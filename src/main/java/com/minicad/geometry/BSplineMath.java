package com.minicad.geometry;

import com.minicad.common.Epsilon;
import com.minicad.common.GeometryException;

import java.util.List;

/**
 * Shared B-spline / NURBS evaluation kernels.
 *
 * <p>The De Boor (Cox-de Boor) routines {@link #findSpan}, {@link #basisValue} and
 * {@link #derivativeBasisValue} are ported verbatim from the previously validated
 * {@code BSplineSurface3} so that curves and surfaces share one source of truth. They
 * operate on the <em>expanded</em> knot vector (multiplicities expanded), which is what
 * {@link BSplineCurve3#expandedKnots()} and the surface's {@code *Expanded()} helpers
 * produce.</p>
 *
 * <p>Curve evaluation is offered in two flavours:
 * <ul>
 *     <li>{@link #evaluate} – non-rational B-spline;</li>
 *     <li>{@link #evaluateRational} – rational B-spline (NURBS) evaluated in
 *     4-D homogeneous space so that control-point weights are honoured.</li>
 * </ul>
 */
public final class BSplineMath {

    private BSplineMath() {
    }

    /**
     * Locates the knot span index {@code i} such that
     * {@code knots[i] <= parameter < knots[i+1]} for a clamped B-spline.
     *
     * @param n number of control points minus one (last valid basis index)
     * @param degree spline degree
     * @param parameter query parameter (already clamped to the valid domain)
     * @param knots expanded knot vector
     * @return span index
     */
    public static int findSpan(int n, int degree, double parameter, List<Double> knots) {
        if (parameter >= knots.get(n + 1)) {
            return n;
        }
        int low = degree;
        int high = n + 1;
        int mid = (low + high) / 2;
        while (parameter < knots.get(mid) || parameter >= knots.get(mid + 1)) {
            if (parameter < knots.get(mid)) {
                high = mid;
            } else {
                low = mid;
            }
            mid = (low + high) / 2;
        }
        return mid;
    }

    /**
     * Values of the {@code degree + 1} non-zero B-spline basis functions over
     * {@code span} at {@code parameter}, computed with the Cox-de Boor triangle
     * (O(degree²), no recursive re-evaluation of shared subproblems).
     *
     * @param span span index from {@link #findSpan}
     * @param parameter query parameter (already clamped to the valid domain)
     * @param degree spline degree
     * @param knots expanded knot vector
     * @return array of {@code degree + 1} values; entry {@code i} belongs to basis index {@code span - degree + i}
     */
    public static double[] basisFunctions(int span, double parameter, int degree, List<Double> knots) {
        double[] values = new double[degree + 1];
        double[] left = new double[degree + 1];
        double[] right = new double[degree + 1];
        values[0] = 1.0;
        for (int j = 1; j <= degree; j++) {
            left[j] = parameter - knots.get(span + 1 - j);
            right[j] = knots.get(span + j) - parameter;
            double saved = 0.0;
            for (int r = 0; r < j; r++) {
                double denominator = right[r + 1] + left[j - r];
                double temp = denominator == 0.0 ? 0.0 : values[r] / denominator;
                values[r] = saved + right[r + 1] * temp;
                saved = left[j - r] * temp;
            }
            values[j] = saved;
        }
        return values;
    }

    /**
     * Value of the {@code i}-th B-spline basis function of the given {@code degree}
     * at {@code parameter}.
     *
     * @param i basis function index
     * @param degree spline degree
     * @param parameter query parameter
     * @param knots expanded knot vector
     * @return basis function value
     */
    public static double basisValue(int i, int degree, double parameter, List<Double> knots) {
        int n = knots.size() - degree - 2;
        int span = findSpan(n, degree, parameter, knots);
        if (i < span - degree || i > span) {
            return 0.0;
        }
        return basisFunctions(span, parameter, degree, knots)[i - (span - degree)];
    }

    /**
     * First derivative of {@link #basisValue} with respect to the parameter.
     *
     * @param i basis function index
     * @param degree spline degree
     * @param parameter query parameter
     * @param knots expanded knot vector
     * @return derivative of the basis function
     */
    public static double derivativeBasisValue(int i, int degree, double parameter, List<Double> knots) {
        double left = 0.0;
        double right = 0.0;
        double leftDenom = knots.get(i + degree) - knots.get(i);
        if (!Epsilon.isZero(leftDenom)) {
            left = degree / leftDenom * basisValue(i, degree - 1, parameter, knots);
        }
        double rightDenom = knots.get(i + degree + 1) - knots.get(i + 1);
        if (!Epsilon.isZero(rightDenom)) {
            right = degree / rightDenom * basisValue(i + 1, degree - 1, parameter, knots);
        }
        return left - right;
    }

    /**
     * Evaluates a non-rational B-spline curve point at {@code parameter}.
     *
     * <p>The parameter is clamped to the valid evaluation domain
     * {@code [knots[degree], knots[controlPoints.size()]]} (expanded indices), matching the
     * surface convention, so queries outside the curve always resolve to an endpoint.</p>
     *
     * @param controlPoints control points (size {@code n + 1})
     * @param degree spline degree
     * @param parameter query parameter
     * @param expandedKnots expanded knot vector (length {@code n + degree + 2})
     * @return point on the curve
     */
    public static CartesianPoint evaluate(List<CartesianPoint> controlPoints, int degree,
                                         double parameter, List<Double> expandedKnots) {
        int n = controlPoints.size() - 1;
        double clamped = clamp(parameter, expandedKnots.get(degree), expandedKnots.get(n + 1));
        int span = findSpan(n, degree, clamped, expandedKnots);
        double[] basis = basisFunctions(span, clamped, degree, expandedKnots);
        double x = 0.0;
        double y = 0.0;
        double z = 0.0;
        for (int i = 0; i <= degree; i++) {
            int index = span - degree + i;
            double b = basis[i];
            CartesianPoint cp = controlPoints.get(index);
            x += b * cp.getX();
            y += b * cp.getY();
            z += b * cp.getZ();
        }
        return new CartesianPoint(x, y, z);
    }

    /**
     * Evaluates a rational B-spline (NURBS) curve point at {@code parameter} in
     * 4-D homogeneous space and projects back to 3-D by dividing by the accumulated
     * weight, so control-point weights are honoured.
     *
     * @param controlPoints control points (size {@code n + 1})
     * @param weights positive control-point weights (same size as control points)
     * @param degree spline degree
     * @param parameter query parameter
     * @param expandedKnots expanded knot vector (length {@code n + degree + 2})
     * @return point on the rational curve
     */
    public static CartesianPoint evaluateRational(List<CartesianPoint> controlPoints, List<Double> weights,
                                                 int degree, double parameter, List<Double> expandedKnots) {
        int n = controlPoints.size() - 1;
        double clamped = clamp(parameter, expandedKnots.get(degree), expandedKnots.get(n + 1));
        int span = findSpan(n, degree, clamped, expandedKnots);
        double[] basis = basisFunctions(span, clamped, degree, expandedKnots);
        double x = 0.0;
        double y = 0.0;
        double z = 0.0;
        double w = 0.0;
        for (int i = 0; i <= degree; i++) {
            int index = span - degree + i;
            double b = basis[i];
            double weight = weights.get(index);
            CartesianPoint cp = controlPoints.get(index);
            x += b * weight * cp.getX();
            y += b * weight * cp.getY();
            z += b * weight * cp.getZ();
            w += b * weight;
        }
        if (!Double.isFinite(w) || w <= 0.0) {
            throw new GeometryException("non-positive homogeneous weight in rational B-spline evaluation");
        }
        return new CartesianPoint(x / w, y / w, z / w);
    }

    private static double clamp(double value, double min, double max) {
        if (max < min) {
            double tmp = min;
            min = max;
            max = tmp;
        }
        return Math.max(min, Math.min(value, max));
    }

    /**
     * Refines a local minimum of a smooth 1-D function with a ternary search.
     * Used to polish the coarse nearest-sample result of parameter-space
     * inversions, whose resolution is otherwise bounded by the sample step.
     *
     * @param distanceAt distance function (assumed unimodal on [lo, hi])
     * @param lo lower bracket
     * @param hi upper bracket
     * @param iterations number of interval-shrinking rounds
     * @return the refined minimizer
     */
    public static double refineLocalMinimum(java.util.function.DoubleUnaryOperator distanceAt,
                                            double lo, double hi, int iterations) {
        for (int i = 0; i < iterations; i++) {
            double third = (hi - lo) / 3.0;
            double m1 = lo + third;
            double m2 = hi - third;
            if (distanceAt.applyAsDouble(m1) <= distanceAt.applyAsDouble(m2)) {
                hi = m2;
            } else {
                lo = m1;
            }
        }
        return (lo + hi) / 2.0;
    }
}
