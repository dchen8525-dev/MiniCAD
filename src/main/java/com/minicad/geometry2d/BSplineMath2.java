package com.minicad.geometry2d;

import com.minicad.common.BSplineCurveDomain;
import com.minicad.common.GeometryException;

import java.util.List;

/**
 * 2D B-spline / NURBS curve evaluation.
 *
 * <p>Only the part that is genuinely two-dimensional lives here: turning the
 * {@code (degree + 1)} non-zero basis values into a {@link Point2}. The window those
 * values were computed over - the clamp into the natural domain, the knot span
 * search and the Cox-de Boor triangle - is dimension-free and is asked for once
 * through {@link BSplineCurveDomain#basisAt(double)}, which
 * {@code com.minicad.geometry.BSplineMath} consumes as well, as do the four curve
 * classes. The knot span search, the Cox-de Boor triangle, the domain clamp and the
 * knot multiplicity expansion it calls are themselves in
 * {@link com.minicad.common.BSplineKernel}.</p>
 *
 * <p>The kernel and the domain deliberately sit in {@code com.minicad.common} rather
 * than in {@code com.minicad.geometry}: this package must not import
 * {@code com.minicad.geometry} at all, because that package depends on this one
 * ({@code SurfaceCurve3} holds a {@code Curve2}). Reaching "up" for either would
 * close a package cycle; duplicating them in both dimensions is what this class
 * used to do.</p>
 */
public final class BSplineMath2 {

    private BSplineMath2() {
    }

    /**
     * Evaluates a non-rational 2D B-spline curve point from the basis window at the
     * query parameter.
     *
     * @param controlPoints control points (size {@code n + 1})
     * @param window the basis values at the query parameter, already clamped into the
     *     expanded domain {@code [knots[degree], knots[n + 1]]}, so a query outside
     *     the curve resolves to an endpoint
     * @return point on the curve
     */
    public static Point2 evaluate(List<Point2> controlPoints, BSplineCurveDomain.BasisAt window) {
        double x = 0.0;
        double y = 0.0;
        for (int i = 0; i < window.width(); i++) {
            double b = window.basis(i);
            Point2 cp = controlPoints.get(window.index(i));
            x += b * cp.getX();
            y += b * cp.getY();
        }
        return new Point2(x, y);
    }

    /**
     * Evaluates a rational 2D B-spline (NURBS) point from the basis window at the
     * query parameter, in homogeneous space so control-point weights are honoured.
     *
     * @param controlPoints control points (size {@code n + 1})
     * @param weights positive control-point weights (same size as control points)
     * @param window the basis values at the query parameter, already clamped into the
     *     expanded domain {@code [knots[degree], knots[n + 1]]}
     * @return point on the rational curve
     */
    public static Point2 evaluateRational(
            List<Point2> controlPoints, List<Double> weights, BSplineCurveDomain.BasisAt window) {
        double x = 0.0;
        double y = 0.0;
        double w = 0.0;
        for (int i = 0; i < window.width(); i++) {
            double b = window.basis(i);
            double weight = weights.get(window.index(i));
            Point2 cp = controlPoints.get(window.index(i));
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
