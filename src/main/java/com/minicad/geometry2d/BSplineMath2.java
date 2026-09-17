package com.minicad.geometry2d;

import com.minicad.common.BSplineKernel;
import com.minicad.common.GeometryException;

import java.util.List;

/**
 * 2D B-spline / NURBS curve evaluation.
 *
 * <p>Only the part that is genuinely two-dimensional lives here: turning the
 * {@code (degree + 1)} non-zero basis values into a {@link Point2}. The knot span
 * search, the Cox-de Boor triangle, the domain clamp and the knot multiplicity
 * expansion are dimension-free and live once in {@link BSplineKernel}, which this
 * class and {@code com.minicad.geometry.BSplineMath} both call.</p>
 *
 * <p>The kernel deliberately sits in {@code com.minicad.common} rather than in
 * {@code com.minicad.geometry}: this package must not import
 * {@code com.minicad.geometry} at all, because that package depends on this one
 * ({@code SurfaceCurve3} holds a {@code Curve2} p-curve). Reaching "up" for the
 * kernel would close a package cycle; duplicating it in both dimensions is what
 * this class used to do.</p>
 */
public final class BSplineMath2 {

    private BSplineMath2() {
    }

    /**
     * Evaluates a non-rational 2D B-spline curve point, clamping the parameter
     * to {@code [knots[degree], knots[controlPoints.size()]]}.
     *
     * @param controlPoints control points (size {@code n + 1})
     * @param degree spline degree
     * @param parameter query parameter
     * @param expandedKnots expanded knot vector (length {@code n + degree + 2})
     * @return point on the curve
     */
    public static Point2 evaluate(List<Point2> controlPoints, int degree, double parameter, List<Double> expandedKnots) {
        int n = controlPoints.size() - 1;
        double clamped = BSplineKernel.clamp(
                parameter, expandedKnots.get(degree), expandedKnots.get(n + 1));
        int span = BSplineKernel.findSpan(n, degree, clamped, expandedKnots);
        double[] basis = BSplineKernel.basisFunctions(span, clamped, degree, expandedKnots);
        double x = 0.0;
        double y = 0.0;
        for (int i = 0; i <= degree; i++) {
            int index = span - degree + i;
            double b = basis[i];
            Point2 cp = controlPoints.get(index);
            x += b * cp.getX();
            y += b * cp.getY();
        }
        return new Point2(x, y);
    }

    /**
     * Evaluates a rational 2D B-spline (NURBS) point in homogeneous space so
     * control-point weights are honoured.
     *
     * @param controlPoints control points (size {@code n + 1})
     * @param weights positive control-point weights (same size as control points)
     * @param degree spline degree
     * @param parameter query parameter
     * @param expandedKnots expanded knot vector (length {@code n + degree + 2})
     * @return point on the rational curve
     */
    public static Point2 evaluateRational(
            List<Point2> controlPoints, List<Double> weights, int degree, double parameter, List<Double> expandedKnots) {
        int n = controlPoints.size() - 1;
        double clamped = BSplineKernel.clamp(
                parameter, expandedKnots.get(degree), expandedKnots.get(n + 1));
        int span = BSplineKernel.findSpan(n, degree, clamped, expandedKnots);
        double[] basis = BSplineKernel.basisFunctions(span, clamped, degree, expandedKnots);
        double x = 0.0;
        double y = 0.0;
        double w = 0.0;
        for (int i = 0; i <= degree; i++) {
            int index = span - degree + i;
            double b = basis[i];
            double weight = weights.get(index);
            Point2 cp = controlPoints.get(index);
            x += b * weight * cp.getX();
            y += b * weight * cp.getY();
            w += b * weight;
        }
        if (!Double.isFinite(w) || w <= 0.0) {
            throw new GeometryException("non-positive homogeneous weight in rational B-spline evaluation");
        }
        return new Point2(x / w, y / w);
    }
}
