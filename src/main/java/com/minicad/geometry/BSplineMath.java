package com.minicad.geometry;

import com.minicad.common.BSplineCurveDomain;
import com.minicad.common.GeometryException;

import java.util.List;

/**
 * 3D B-spline / NURBS curve evaluation.
 *
 * <p>Only the part that is genuinely three-dimensional lives here: turning the
 * {@code (degree + 1)} non-zero basis values into a {@link CartesianPoint}. The
 * window those values were computed over - the clamp into the natural domain, the
 * knot span search and the Cox-de Boor triangle - is dimension-free and is asked
 * for once through {@link BSplineCurveDomain#basisAt(double)}, which
 * {@code com.minicad.geometry2d.BSplineMath2} consumes as well, as do the four curve
 * classes. Before that the window was opened four times: here, in the 2D evaluator,
 * and once per flavour again. The knot span search, the Cox-de Boor triangle, the
 * domain clamp and the knot multiplicity expansion it calls are themselves in
 * {@link com.minicad.common.BSplineKernel}.</p>
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
     * Evaluates a non-rational B-spline curve point from the basis window at the
     * query parameter.
     *
     * @param controlPoints control points (size {@code n + 1})
     * @param window the basis values at the query parameter, already clamped into the
     *     expanded domain {@code [knots[degree], knots[n + 1]]}, so a query outside
     *     the curve resolves to an endpoint
     * @return point on the curve
     */
    public static CartesianPoint evaluate(List<CartesianPoint> controlPoints, BSplineCurveDomain.BasisAt window) {
        double x = 0.0;
        double y = 0.0;
        double z = 0.0;
        for (int i = 0; i < window.width(); i++) {
            double b = window.basis(i);
            CartesianPoint cp = controlPoints.get(window.index(i));
            x += b * cp.getX();
            y += b * cp.getY();
            z += b * cp.getZ();
        }
        return new CartesianPoint(x, y, z);
    }

    /**
     * Evaluates a rational B-spline (NURBS) curve point from the basis window at the
     * query parameter, in 4-D homogeneous space, and projects back to 3-D by dividing
     * by the accumulated weight, so control-point weights are honoured.
     *
     * @param controlPoints control points (size {@code n + 1})
     * @param weights positive control-point weights (same size as control points)
     * @param window the basis values at the query parameter, already clamped into the
     *     expanded domain {@code [knots[degree], knots[n + 1]]}
     * @return point on the rational curve
     */
    public static CartesianPoint evaluateRational(List<CartesianPoint> controlPoints, List<Double> weights,
                                                 BSplineCurveDomain.BasisAt window) {
        double x = 0.0;
        double y = 0.0;
        double z = 0.0;
        double w = 0.0;
        for (int i = 0; i < window.width(); i++) {
            double b = window.basis(i);
            double weight = weights.get(window.index(i));
            CartesianPoint cp = controlPoints.get(window.index(i));
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
