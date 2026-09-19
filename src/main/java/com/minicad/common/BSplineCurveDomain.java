package com.minicad.common;

import java.util.List;

/**
 * The parameter domain of a B-spline curve: its degree, its control-point count,
 * its knot vector, and the non-zero basis values at a parameter.
 *
 * <p>Four curve classes hold one of these - {@code geometry.BSplineCurve3},
 * {@code geometry.RationalBSplineCurve3}, {@code geometry2d.BSplineCurve2} and
 * {@code geometry2d.RationalBSplineCurve2} - and they ask it every question they
 * used to answer separately:</p>
 * <ul>
 *   <li>whether a degree / control-point count / knot vector combination is
 *       usable, which each dimension wrote out once and the other dimension's
 *       rational variant inherited by calling it;</li>
 *   <li>the natural parameter domain ({@link #startParameter()} /
 *       {@link #endParameter()}) and the multiplicity-expanded knot vector
 *       ({@link #expanded()}), which appeared once per class;</li>
 *   <li>the basis window at a parameter ({@link #basisAt(double)}), which the two
 *       dimension-specific evaluators each opened with twice - once for the
 *       non-rational curve and once for the rational one.</li>
 * </ul>
 *
 * <p>It cannot be a base class. The curve dispatch tables key the rational and
 * non-rational classes separately - {@code BSplineCurve3.class} next to
 * {@code RationalBSplineCurve3.class} in the edge-sample, transform and
 * projection rules, {@code BSplineCurve2.class} next to
 * {@code RationalBSplineCurve2.class} in the p-curve samplers - so making one a
 * subtype of the other would let a rational instance match the non-rational rule
 * first and silently take that branch. Composition is the only route, which is
 * what this class provides; {@code geometry.BSplineSurfaceDomain} is the same
 * shape for the two-parameter case.</p>
 *
 * <p>It lives in {@code com.minicad.common} because the two curve dimensions sit
 * in packages that must not depend on each other: {@code com.minicad.geometry}
 * already depends on {@code com.minicad.geometry2d} ({@code SurfaceCurve3} holds a
 * {@code Curve2}), so a home in either one would close a package cycle. The knot
 * vector itself and the basis arithmetic are dimension-free for the same reason
 * and are in {@link BSplineKernel} / {@link KnotVector} next door.</p>
 *
 * <p><b>Why this check is the stricter of the family's two.</b> A curve's knots
 * always arrive as distinct values paired with multiplicities, so
 * {@link #of} can require them to be <em>strictly increasing</em>. Surfaces accept
 * a second encoding as well - an already-expanded flat vector with unit
 * multiplicities, routine for a clamped vector - and are therefore limited to
 * non-decreasing. The two rules were written as one until the surface side had to
 * be relaxed; keeping them apart is deliberate, and
 * {@code BSplineSurfaceDomain}'s javadoc carries the other half of the
 * argument.</p>
 */
public final class BSplineCurveDomain {

    private final int degree;
    private final int controlPointCount;
    private final KnotVector knotVector;

    private BSplineCurveDomain(int degree, int controlPointCount, KnotVector knotVector) {
        this.degree = degree;
        this.controlPointCount = controlPointCount;
        this.knotVector = knotVector;
    }

    /**
     * Validates a degree, a control-point list and a knot vector against each other
     * and returns the domain they describe.
     *
     * <p>The checks run in one fixed order - degree, control-point count, knot and
     * multiplicity sizes, knot monotonicity, multiplicity positivity, expanded
     * count - so that all four curve classes report the same problem for the same
     * malformed input. The control points are taken as a list rather than as a
     * count so that "absent" and "too few" are the same rejection here instead of a
     * null test the callers each wrote around their copy of this sequence.</p>
     *
     * <p>The degree message is the one the 2D copy had. The two copies had drifted
     * on it - the 3D one said only "B-spline degree must be positive", the 2D one
     * named the offending value - while rejecting exactly the same inputs. Nothing
     * observed the text, which is why it could drift at all; the informative form is
     * the one kept.</p>
     *
     * @param degree spline degree
     * @param controlPoints control points, read for their count only
     * @param knotMultiplicities multiplicity of each unique knot value
     * @param knots unique knot values, strictly increasing
     * @return the validated domain
     * @throws GeometryException when the combination cannot be evaluated
     */
    public static BSplineCurveDomain of(int degree, List<?> controlPoints,
                                        List<Integer> knotMultiplicities, List<Double> knots) {
        if (degree < 1) {
            throw new GeometryException("B-spline degree must be at least 1, got " + degree);
        }
        if (controlPoints == null || controlPoints.size() <= degree) {
            throw new GeometryException("B-spline requires more control points than its degree");
        }
        if (knots == null || knotMultiplicities == null || knots.size() != knotMultiplicities.size()) {
            throw new GeometryException("knot values and multiplicities must have equal size");
        }
        int expandedCount = 0;
        double previous = Double.NEGATIVE_INFINITY;
        for (int index = 0; index < knots.size(); index++) {
            double knot = knots.get(index);
            int multiplicity = knotMultiplicities.get(index);
            if (!Double.isFinite(knot) || knot <= previous) {
                throw new GeometryException("knot values must be finite and strictly increasing");
            }
            if (multiplicity <= 0) {
                throw new GeometryException("knot multiplicities must be positive");
            }
            previous = knot;
            expandedCount += multiplicity;
        }
        if (expandedCount != controlPoints.size() + degree + 1) {
            throw new GeometryException("expanded knot count does not match control points and degree");
        }
        return new BSplineCurveDomain(
                degree, controlPoints.size(), new KnotVector(knots, knotMultiplicities));
    }

    /**
     * Validates the weights of a rational curve against its control points and
     * returns them as an immutable list.
     *
     * <p>Weights belong to a rational curve's definition - one per control point,
     * finite and positive - so they are checked here next to the rest of it, and
     * the two rational classes in the two dimensions no longer each spell the check
     * out. They are returned rather than kept because the domain holds only what
     * the rational and the non-rational curve have in common, and a non-rational
     * curve has no weights.</p>
     *
     * @param controlPoints control points the weights must correspond to
     * @param weights one weight per control point
     * @return the weights as an immutable list
     * @throws GeometryException when there is not exactly one finite positive weight per control point
     */
    public static List<Double> validatedWeights(List<?> controlPoints, List<Double> weights) {
        if (weights == null || controlPoints == null || weights.size() != controlPoints.size()) {
            throw new GeometryException("weight count must match control point count");
        }
        for (double weight : weights) {
            if (!Double.isFinite(weight) || weight <= 0.0) {
                throw new GeometryException("weights must be finite and positive");
            }
        }
        return List.copyOf(weights);
    }

    /**
     * @return spline degree
     */
    public int degree() {
        return degree;
    }

    /**
     * @return number of control points the knot vector is consistent with
     */
    public int controlPointCount() {
        return controlPointCount;
    }

    /**
     * @return number of unique knot values
     */
    public int knotCount() {
        return knotVector.knots().size();
    }

    /**
     * @return the unique knot values
     */
    public List<Double> knots() {
        return knotVector.knots();
    }

    /**
     * @return the multiplicity of each unique knot value
     */
    public List<Integer> multiplicities() {
        return knotVector.multiplicities();
    }

    /**
     * First unique knot value - the start of the domain a curve reports, and the
     * parameter a clamped curve reaches its first control point at.
     *
     * @return start of the natural parameter domain
     */
    public double startParameter() {
        return knotVector.start();
    }

    /**
     * Last unique knot value - the end of the domain a curve reports.
     *
     * @return end of the natural parameter domain
     */
    public double endParameter() {
        return knotVector.end();
    }

    /**
     * The multiplicity-expanded knot vector. Its expansion is cached inside the
     * knot vector after first use, which is what keeps evaluation off the
     * allocation path.
     *
     * @return the expanded knot vector
     */
    public List<Double> expanded() {
        return knotVector.expanded();
    }

    /**
     * Clamps the parameter into the natural domain, locates the knot span and
     * evaluates the non-zero basis functions over it.
     *
     * <p>Both clamp bounds come from the expanded vector - its entries at the degree
     * and at the control-point count - and not from {@link #startParameter()} /
     * {@link #endParameter()}. For a clamped vector the two readings coincide; for
     * an unclamped one they do not, and the span search assumes the parameter it
     * receives lies inside the knot range or it does not terminate. This is the
     * window the dimension-specific evaluators used before this class existed, and
     * it has to stay exactly that window.</p>
     *
     * @param parameter query parameter, clamped to the natural domain
     * @return the span and the basis values at the clamped parameter
     */
    public BasisAt basisAt(double parameter) {
        List<Double> expandedKnots = knotVector.expanded();
        double clamped = BSplineKernel.clamp(
                parameter, expandedKnots.get(degree), expandedKnots.get(controlPointCount));
        int span = BSplineKernel.findSpan(controlPointCount - 1, degree, clamped, expandedKnots);
        return new BasisAt(degree, span, BSplineKernel.basisFunctions(span, clamped, degree, expandedKnots));
    }

    /**
     * The non-zero basis values at one parameter together with the span they are
     * anchored at.
     *
     * <p>Everything the accumulation loops of the two curve dimensions need is on
     * this side of the seam: they index control points, weights and basis values
     * through {@link #index(int)}, which is exactly the arithmetic that used to be
     * repeated in each of the four evaluators. The clamped parameter itself is not
     * kept, because no accumulation reads it.</p>
     */
    public static final class BasisAt {

        private final int degree;
        private final int span;
        private final double[] basis;

        private BasisAt(int degree, int span, double[] basis) {
            this.degree = degree;
            this.span = span;
            this.basis = basis;
        }

        /**
         * @return number of non-zero basis functions, i.e. the degree plus one
         */
        public int width() {
            return basis.length;
        }

        /**
         * @param i offset within the non-zero basis window
         * @return the control-point index that {@code i} belongs to
         */
        public int index(int i) {
            return span - degree + i;
        }

        /**
         * @param i offset within the non-zero basis window
         * @return the basis value for that index
         */
        public double basis(int i) {
            return basis[i];
        }
    }
}
