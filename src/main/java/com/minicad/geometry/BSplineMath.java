package com.minicad.geometry;

import com.minicad.common.BSplineKernel;
import com.minicad.common.GeometryException;

import java.util.List;

/**
 * 3D B-spline / NURBS curve evaluation.
 *
 * <p>Only the part that is genuinely three-dimensional lives here: turning the
 * {@code (degree + 1)} non-zero basis values into a {@link CartesianPoint}. The
 * basis values themselves, the knot span search, the domain clamp and the knot
 * multiplicity expansion are dimension-free and live once in
 * {@link BSplineKernel}, which both this class and
 * {@code com.minicad.geometry2d.BSplineMath2} call. Surfaces evaluate through the
 * kernel directly.</p>
 *
 * <p>Evaluation is offered in two flavours:
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
        double clamped = BSplineKernel.clamp(
                parameter, expandedKnots.get(degree), expandedKnots.get(n + 1));
        int span = BSplineKernel.findSpan(n, degree, clamped, expandedKnots);
        double[] basis = BSplineKernel.basisFunctions(span, clamped, degree, expandedKnots);
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
        double clamped = BSplineKernel.clamp(
                parameter, expandedKnots.get(degree), expandedKnots.get(n + 1));
        int span = BSplineKernel.findSpan(n, degree, clamped, expandedKnots);
        double[] basis = BSplineKernel.basisFunctions(span, clamped, degree, expandedKnots);
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
}
