package com.minicad.geometry;

import com.minicad.common.Epsilon;
import com.minicad.common.GeometryException;
import com.minicad.common.Preconditions;

/**
 * Minimal 3D circle representation.
 *
 * @param position circle placement
 * @param radius positive radius
 */
public record Circle(Axis2Placement3D position, double radius) implements Curve3 {

    /**
     * Creates a circle and validates its invariants.
     */
    public Circle {
        Preconditions.requireNonNull(position, "position");
        Preconditions.requireFinite(radius, "radius");
        if (radius <= Epsilon.EPS) {
            throw new GeometryException("radius must be greater than epsilon");
        }
    }

    /**
     * Evaluates a point on the circle using the placement's natural orientation.
     *
     * @param angle angle in radians from the local X axis
     * @return point on the circle
     */
    public CartesianPoint pointAt(double angle) {
        Preconditions.requireFinite(angle, "angle");
        Vector3 radial = position.xDirection().asVector().scale(Math.cos(angle) * radius)
                .add(position.yDirection().asVector().scale(Math.sin(angle) * radius));
        return position.location().add(radial);
    }

    @Override
    public boolean contains(CartesianPoint point) {
        Preconditions.requireNonNull(point, "point");
        Vector3 offset = point.subtract(position.location());
        double planeDistance = Math.abs(offset.dot(position.axis().asVector()));
        if (planeDistance > Epsilon.EPS) {
            return false;
        }
        return Epsilon.equals(offset.norm(), radius);
    }

    /**
     * Returns the natural parameter angle for a point on the circle.
     *
     * @param point point on the circle
     * @return angle in radians in [0, 2pi)
     */
    public double angleOf(CartesianPoint point) {
        if (!contains(point)) {
            throw new GeometryException("point must lie on the circle");
        }
        Vector3 offset = point.subtract(position.location());
        double x = offset.dot(position.xDirection().asVector());
        double y = offset.dot(position.yDirection().asVector());
        double angle = Math.atan2(y, x);
        return angle >= 0.0 ? angle : angle + Math.PI * 2.0;
    }
}
