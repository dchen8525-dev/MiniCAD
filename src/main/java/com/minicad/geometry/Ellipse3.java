package com.minicad.geometry;

import com.minicad.common.Epsilon;
import com.minicad.common.GeometryException;
import com.minicad.common.Preconditions;

/**
 * Minimal 3D ellipse representation.
 *
 * @param position ellipse placement
 * @param semiAxis1 semi-axis along local X
 * @param semiAxis2 semi-axis along local Y
 */
public record Ellipse3(Axis2Placement3D position, double semiAxis1, double semiAxis2) implements Curve3 {

    /**
     * Creates an ellipse and validates its invariants.
     */
    public Ellipse3 {
        Preconditions.requireNonNull(position, "position");
        Preconditions.requireFinite(semiAxis1, "semiAxis1");
        Preconditions.requireFinite(semiAxis2, "semiAxis2");
        if (semiAxis1 <= Epsilon.EPS || semiAxis2 <= Epsilon.EPS) {
            throw new GeometryException("ellipse semi-axes must be greater than epsilon");
        }
    }

    /**
     * Evaluates a point on the ellipse.
     *
     * @param angle angle in radians from local X
     * @return point on the ellipse
     */
    public CartesianPoint pointAt(double angle) {
        Preconditions.requireFinite(angle, "angle");
        Vector3 offset = position.xDirection().asVector().scale(Math.cos(angle) * semiAxis1)
                .add(position.yDirection().asVector().scale(Math.sin(angle) * semiAxis2));
        return position.location().add(offset);
    }

    @Override
    public boolean contains(CartesianPoint point) {
        Preconditions.requireNonNull(point, "point");
        Vector3 offset = point.subtract(position.location());
        double planeDistance = Math.abs(offset.dot(position.axis().asVector()));
        if (planeDistance > Epsilon.EPS) {
            return false;
        }
        double x = offset.dot(position.xDirection().asVector()) / semiAxis1;
        double y = offset.dot(position.yDirection().asVector()) / semiAxis2;
        return Epsilon.equals(x * x + y * y, 1.0);
    }

    /**
     * Returns the natural parameter angle for a point on the ellipse.
     *
     * @param point point on the ellipse
     * @return angle in radians in [0, 2pi)
     */
    public double angleOf(CartesianPoint point) {
        if (!contains(point)) {
            throw new GeometryException("point must lie on the ellipse");
        }
        Vector3 offset = point.subtract(position.location());
        double x = offset.dot(position.xDirection().asVector()) / semiAxis1;
        double y = offset.dot(position.yDirection().asVector()) / semiAxis2;
        double angle = Math.atan2(y, x);
        return angle >= 0.0 ? angle : angle + Math.PI * 2.0;
    }
}
