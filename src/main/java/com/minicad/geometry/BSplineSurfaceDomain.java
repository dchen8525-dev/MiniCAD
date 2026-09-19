package com.minicad.geometry;

import com.minicad.common.BSplineKernel;
import com.minicad.common.GeometryException;
import com.minicad.common.KnotVector;

import java.util.List;

/**
 * The {@code (u, v)} parameter domain of a tensor-product B-spline surface.
 *
 * <p>Both surface classes - {@link BSplineSurface3} and
 * {@link RationalBSplineSurface3} - hold one of these and ask it every question
 * about their domain: the two degrees, the two control-point counts, the natural
 * domain in each direction, and the basis values at a parameter pair. Before this
 * class existed the two agreed on all of it by having written it out twice. Their
 * constructors spelled out the same validation sequence, and {@code pointAt} and
 * {@code normalAt} each opened with the same ten-line "expand, clamp, find span,
 * evaluate basis" prologue, so that prologue appeared four times.</p>
 *
 * <p>They cannot share a base class instead. The surface dispatch tables key on
 * {@code BSplineSurface3.class} and {@code RationalBSplineSurface3.class}
 * separately, so making one a subtype of the other would let a rational instance
 * match the non-rational rule first and silently take that branch. Composition is
 * the only route, which is what this class provides.</p>
 *
 * <p><b>Why the knot check here is the weaker of the family's two.</b> The curve
 * classes require knot values to be <em>strictly increasing</em>, because a curve's
 * knots are always distinct values paired with multiplicities. A surface knot list
 * reaches the surface classes in either of two encodings, and the second one repeats
 * its values:</p>
 * <ul>
 *   <li>distinct values with multiplicities, which is what STEP's
 *       {@code B_SPLINE_SURFACE_WITH_KNOTS} carries;</li>
 *   <li>an already-expanded flat vector with unit multiplicities, which is what
 *       {@code StepCadBuilder.buildFreeFormSurface} slices out of a
 *       {@code FREE_FORM_SURFACE}'s concatenated knot vectors. A clamped flat vector
 *       such as {@code [0, 0, 0, 1, 2, 2, 2]} is ordinary there.</li>
 * </ul>
 * <p>Demanding distinct values would reject the second encoding outright, so the
 * check stays non-decreasing on purpose rather than by omission. The corpus does not
 * choose between the two readings: across the 45 bundled STEP files every one of the
 * 174 surface knot lists is strictly increasing already.</p>
 */
final class BSplineSurfaceDomain {

    private final int uDegree;
    private final int vDegree;
    private final int uCount;
    private final int vCount;
    private final KnotVector uKnotVector;
    private final KnotVector vKnotVector;

    private BSplineSurfaceDomain(
            int uDegree,
            int vDegree,
            int uCount,
            int vCount,
            KnotVector uKnotVector,
            KnotVector vKnotVector
    ) {
        this.uDegree = uDegree;
        this.vDegree = vDegree;
        this.uCount = uCount;
        this.vCount = vCount;
        this.uKnotVector = uKnotVector;
        this.vKnotVector = vKnotVector;
    }

    /**
     * Validates a control-point grid against its degrees and both knot vectors, and
     * returns the domain they describe.
     *
     * <p>The checks run in one fixed order - degrees, control-point counts, row
     * uniformity, knot sizes, knot monotonicity - so that the two surface classes
     * report the same problem for the same malformed input. Each of them used to spell
     * this sequence out separately, which is how a message could drift between the
     * non-rational and rational variants of the same check.</p>
     *
     * @param uDegree U degree
     * @param vDegree V degree
     * @param controlPoints control-point grid, one list per U position
     * @param uKnots U knot values
     * @param uMultiplicities multiplicity of each U knot value
     * @param vKnots V knot values
     * @param vMultiplicities multiplicity of each V knot value
     * @return the validated domain
     */
    static BSplineSurfaceDomain of(
            int uDegree,
            int vDegree,
            List<List<CartesianPoint>> controlPoints,
            List<Double> uKnots,
            List<Integer> uMultiplicities,
            List<Double> vKnots,
            List<Integer> vMultiplicities
    ) {
        if (uDegree < 1 || vDegree < 1) {
            throw new GeometryException("surface degrees must be at least 1");
        }
        int uCount = controlPoints.size();
        if (uCount < uDegree + 1) {
            throw new GeometryException("U control-point count must be at least degree + 1");
        }
        int vCount = controlPoints.get(0).size();
        if (vCount < vDegree + 1) {
            throw new GeometryException("V control-point count must be at least degree + 1");
        }
        for (List<CartesianPoint> row : controlPoints) {
            if (row.size() != vCount) {
                throw new GeometryException("control-point rows must have uniform length");
            }
        }
        if (uMultiplicities.size() != uKnots.size() || vMultiplicities.size() != vKnots.size()) {
            throw new GeometryException("knot multiplicities and knot values must have matching sizes");
        }
        validateKnots(uDegree, uCount, uKnots, uMultiplicities);
        validateKnots(vDegree, vCount, vKnots, vMultiplicities);
        return new BSplineSurfaceDomain(
                uDegree,
                vDegree,
                uCount,
                vCount,
                new KnotVector(uKnots, uMultiplicities),
                new KnotVector(vKnots, vMultiplicities));
    }

    /**
     * Rejects knot vectors that are unusable for evaluation, in one of the two
     * encodings described on this class: knot values must be finite and
     * non-decreasing, every multiplicity must be positive, and the expanded vector
     * must be exactly as long as the control-point count plus the degree plus one.
     *
     * @param degree spline degree in one direction
     * @param controlPointCount control-point count in the same direction
     * @param knots knot values, one entry per (possibly repeated) value
     * @param multiplicities multiplicity per entry of {@code knots}
     */
    private static void validateKnots(
            int degree,
            int controlPointCount,
            List<Double> knots,
            List<Integer> multiplicities
    ) {
        int expandedCount = 0;
        double previous = Double.NEGATIVE_INFINITY;
        for (int index = 0; index < knots.size(); index++) {
            double knot = knots.get(index);
            if (!Double.isFinite(knot)) {
                throw new GeometryException("knot values must be finite");
            }
            if (knot < previous) {
                throw new GeometryException("knot values must be nondecreasing");
            }
            int multiplicity = multiplicities.get(index);
            if (multiplicity < 1) {
                throw new GeometryException("knot multiplicities must be positive");
            }
            expandedCount += multiplicity;
            previous = knot;
        }
        int expected = controlPointCount + degree + 1;
        if (expandedCount != expected) {
            throw new GeometryException("expanded knot count must equal control point count + degree + 1");
        }
    }

    int uDegree() {
        return uDegree;
    }

    int vDegree() {
        return vDegree;
    }

    int uCount() {
        return uCount;
    }

    int vCount() {
        return vCount;
    }

    List<Integer> uMultiplicities() {
        return uKnotVector.multiplicities();
    }

    List<Integer> vMultiplicities() {
        return vKnotVector.multiplicities();
    }

    List<Double> uKnots() {
        return uKnotVector.knots();
    }

    List<Double> vKnots() {
        return vKnotVector.knots();
    }

    /**
     * @return start of the natural U domain, read from the expanded knot vector
     */
    double uStart() {
        return uKnotVector.expandedStart(uDegree);
    }

    /**
     * @return end of the natural U domain, read from the expanded knot vector
     */
    double uEnd() {
        return uKnotVector.expandedEnd(uCount);
    }

    /**
     * @return start of the natural V domain, read from the expanded knot vector
     */
    double vStart() {
        return vKnotVector.expandedStart(vDegree);
    }

    /**
     * @return end of the natural V domain, read from the expanded knot vector
     */
    double vEnd() {
        return vKnotVector.expandedEnd(vCount);
    }

    /**
     * Expands both knot vectors, clamps the query into the natural domain, locates the
     * two spans and evaluates the non-zero basis functions over them.
     *
     * <p>This is the one step the two surfaces could not share by delegating through
     * {@code pointAt}: it is upstream of both {@code pointAt} and {@code normalAt}, and
     * the derivative evaluation needs the clamped parameter and the expanded vector
     * rather than just the values, which is why they travel back together with the
     * basis in {@link BasisAt}.</p>
     *
     * <p>Both clamp bounds are the expanded vector's entries at the degree and at the
     * control-point count - not the first and last unique knot, which coincide with them
     * only for a clamped vector. That is the window the two classes reported before this
     * class existed, and it has to stay that way: the span search assumes the parameter it
     * receives lies inside the knot range and does not terminate otherwise, so widening
     * these bounds hangs instead of raising.</p>
     *
     * @param u U parameter, clamped to the natural U domain
     * @param v V parameter, clamped to the natural V domain
     * @return the spans, clamped parameters and basis values at {@code (u, v)}
     */
    BasisAt basisAt(double u, double v) {
        List<Double> uExpanded = uKnotVector.expanded();
        List<Double> vExpanded = vKnotVector.expanded();
        double clampedU = BSplineKernel.clamp(u, uExpanded.get(uDegree), uExpanded.get(uCount));
        double clampedV = BSplineKernel.clamp(v, vExpanded.get(vDegree), vExpanded.get(vCount));
        int uSpan = BSplineKernel.findSpan(uCount - 1, uDegree, clampedU, uExpanded);
        int vSpan = BSplineKernel.findSpan(vCount - 1, vDegree, clampedV, vExpanded);
        return new BasisAt(
                uDegree,
                vDegree,
                uSpan,
                vSpan,
                clampedU,
                clampedV,
                uExpanded,
                vExpanded,
                BSplineKernel.basisFunctions(uSpan, clampedU, uDegree, uExpanded),
                BSplineKernel.basisFunctions(vSpan, clampedV, vDegree, vExpanded));
    }

    /**
     * The non-zero basis values at one {@code (u, v)} position, together with the spans
     * and clamped parameters they were computed from.
     *
     * <p>Returning the spans alongside the values is what lets {@code pointAt} and
     * {@code normalAt} reduce to their accumulation loops: each of them indexes control
     * points, basis values and basis derivatives through the same {@code (span, i)}
     * arithmetic, and that arithmetic is on this side of the seam.</p>
     */
    static final class BasisAt {

        private final int uDegree;
        private final int vDegree;
        private final int uSpan;
        private final int vSpan;
        private final double clampedU;
        private final double clampedV;
        private final List<Double> uExpanded;
        private final List<Double> vExpanded;
        private final double[] uBasis;
        private final double[] vBasis;

        private BasisAt(
                int uDegree,
                int vDegree,
                int uSpan,
                int vSpan,
                double clampedU,
                double clampedV,
                List<Double> uExpanded,
                List<Double> vExpanded,
                double[] uBasis,
                double[] vBasis
        ) {
            this.uDegree = uDegree;
            this.vDegree = vDegree;
            this.uSpan = uSpan;
            this.vSpan = vSpan;
            this.clampedU = clampedU;
            this.clampedV = clampedV;
            this.uExpanded = uExpanded;
            this.vExpanded = vExpanded;
            this.uBasis = uBasis;
            this.vBasis = vBasis;
        }

        /**
         * @param i offset within the non-zero basis window
         * @return the control-point index in U that {@code i} belongs to
         */
        int uIndex(int i) {
            return uSpan - uDegree + i;
        }

        /**
         * @param j offset within the non-zero basis window
         * @return the control-point index in V that {@code j} belongs to
         */
        int vIndex(int j) {
            return vSpan - vDegree + j;
        }

        /**
         * @param i offset within the non-zero basis window
         * @return the basis value for that U index
         */
        double uBasis(int i) {
            return uBasis[i];
        }

        /**
         * @param j offset within the non-zero basis window
         * @return the basis value for that V index
         */
        double vBasis(int j) {
            return vBasis[j];
        }

        /**
         * @param i offset within the non-zero basis window
         * @return the parameter derivative of the basis function at that U index
         */
        double uDerivative(int i) {
            return BSplineKernel.derivativeBasisValue(uSpan - uDegree + i, uDegree, clampedU, uExpanded);
        }

        /**
         * @param j offset within the non-zero basis window
         * @return the parameter derivative of the basis function at that V index
         */
        double vDerivative(int j) {
            return BSplineKernel.derivativeBasisValue(vSpan - vDegree + j, vDegree, clampedV, vExpanded);
        }
    }
}
