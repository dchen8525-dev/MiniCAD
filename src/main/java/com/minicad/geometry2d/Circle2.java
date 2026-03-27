package com.minicad.geometry2d;

import com.minicad.common.Epsilon;
import com.minicad.common.GeometryException;
import com.minicad.common.Preconditions;

/**
 * Minimal 2D circle representation.
 *
 * @param center circle center
 * @param xDirection local x direction
 * @param radius positive radius
 */
public record Circle2(Point2 center, Direction2 xDirection, double radius) implements Curve2 {

    public Circle2 {
        Preconditions.requireNonNull(center, "center");
        Preconditions.requireNonNull(xDirection, "xDirection");
        Preconditions.requireFinite(radius, "radius");
        if (radius <= Epsilon.EPS) {
            throw new GeometryException("radius must be greater than epsilon");
        }
    }

    public Point2 pointAt(double angle) {
        Preconditions.requireFinite(angle, "angle");
        Vector2 x = xDirection.asVector();
        Vector2 y = new Vector2(-x.y(), x.x());
        return center.add(x.scale(Math.cos(angle) * radius).add(y.scale(Math.sin(angle) * radius)));
    }

    public double angleOf(Point2 point) {
        Preconditions.requireNonNull(point, "point");
        if (!contains(point)) {
            throw new GeometryException("point must lie on the circle");
        }
        Vector2 offset = point.subtract(center);
        Vector2 x = xDirection.asVector();
        Vector2 y = new Vector2(-x.y(), x.x());
        double angle = Math.atan2(offset.dot(y), offset.dot(x));
        return angle >= 0.0 ? angle : angle + Math.PI * 2.0;
    }

    @Override
    public boolean contains(Point2 point) {
        Preconditions.requireNonNull(point, "point");
        return Math.abs(point.subtract(center).norm() - radius) <= Epsilon.EPS;
    }
}
