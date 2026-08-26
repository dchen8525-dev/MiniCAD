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
        if (degree == 0) {
            if ((parameter >= knots.get(i) && parameter < knots.get(i + 1))
                    || (Epsilon.equals(parameter, knots.get(knots.size() - 1)) && Epsilon.equals(parameter, knots.get(i + 1)))) {
                return 1.0;
            }
            return 0.0;
        }
        double leftDenominator = knots.get(i + degree) - knots.get(i);
        double rightDenominator = knots.get(i + degree + 1) - knots.get(i + 1);
        double left = Epsilon.isZero(leftDenominator)
                ? 0.0
                : (parameter - knots.get(i)) / leftDenominator * basisValue(i, degree - 1, parameter, knots);
        double right = Epsilon.isZero(rightDenominator)
                ? 0.0
                : (knots.get(i + degree + 1) - parameter) / rightDenominator * basisValue(i + 1, degree - 1, parameter, knots);
        return left + right;
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
        double x = 0.0;
        double y = 0.0;
        double z = 0.0;
        for (int i = 0; i <= degree; i++) {
            int index = span - degree + i;
            double basis = basisValue(index, degree, clamped, expandedKnots);
            CartesianPoint cp = controlPoints.get(index);
            x += basis * cp.getX();
            y += basis * cp.getY();
            z += basis * cp.getZ();
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
        double x = 0.0;
        double y = 0.0;
        double z = 0.0;
        double w = 0.0;
        for (int i = 0; i <= degree; i++) {
            int index = span - degree + i;
            double basis = basisValue(index, degree, clamped, expandedKnots);
            double weight = weights.get(index);
            CartesianPoint cp = controlPoints.get(index);
            x += basis * weight * cp.getX();
            y += basis * weight * cp.getY();
            z += basis * weight * cp.getZ();
            w += basis * weight;
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
}
