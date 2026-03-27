package com.minicad.geometry2d;

import com.minicad.common.Epsilon;
import com.minicad.common.GeometryException;
import com.minicad.common.Preconditions;

/**
 * Minimal 2D ellipse representation.
 *
 * @param center ellipse center
 * @param xDirection local x direction
 * @param semiAxis1 local x semi-axis
 * @param semiAxis2 local y semi-axis
 */
public record Ellipse2(Point2 center, Direction2 xDirection, double semiAxis1, double semiAxis2) implements Curve2 {

    public Ellipse2 {
        Preconditions.requireNonNull(center, "center");
        Preconditions.requireNonNull(xDirection, "xDirection");
        Preconditions.requireFinite(semiAxis1, "semiAxis1");
        Preconditions.requireFinite(semiAxis2, "semiAxis2");
        if (semiAxis1 <= Epsilon.EPS || semiAxis2 <= Epsilon.EPS) {
            throw new GeometryException("ellipse semi axes must be greater than epsilon");
        }
    }

    public Point2 pointAt(double angle) {
        Preconditions.requireFinite(angle, "angle");
        Vector2 x = xDirection.asVector();
        Vector2 y = new Vector2(-x.y(), x.x());
        return center.add(x.scale(Math.cos(angle) * semiAxis1).add(y.scale(Math.sin(angle) * semiAxis2)));
    }

    public double angleOf(Point2 point) {
        Preconditions.requireNonNull(point, "point");
        if (!contains(point)) {
            throw new GeometryException("point must lie on the ellipse");
        }
        Vector2 offset = point.subtract(center);
        Vector2 x = xDirection.asVector();
        Vector2 y = new Vector2(-x.y(), x.x());
        double normalizedX = offset.dot(x) / semiAxis1;
        double normalizedY = offset.dot(y) / semiAxis2;
        double angle = Math.atan2(normalizedY, normalizedX);
        return angle >= 0.0 ? angle : angle + Math.PI * 2.0;
    }

    @Override
    public boolean contains(Point2 point) {
        Preconditions.requireNonNull(point, "point");
        Vector2 offset = point.subtract(center);
        Vector2 x = xDirection.asVector();
        Vector2 y = new Vector2(-x.y(), x.x());
        double normalizedX = offset.dot(x) / semiAxis1;
        double normalizedY = offset.dot(y) / semiAxis2;
        return Math.abs((normalizedX * normalizedX) + (normalizedY * normalizedY) - 1.0) <= 1.0e-9;
    }
}
