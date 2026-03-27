package com.minicad.geometry2d;

import com.minicad.common.Epsilon;
import com.minicad.common.Preconditions;

/**
 * Infinite 2D line.
 *
 * @param origin line origin
 * @param direction line direction
 */
public record Line2(Point2 origin, Direction2 direction) implements Curve2 {

    public Line2 {
        Preconditions.requireNonNull(origin, "origin");
        Preconditions.requireNonNull(direction, "direction");
    }

    public Point2 pointAt(double parameter) {
        Preconditions.requireFinite(parameter, "parameter");
        return origin.add(direction.asVector().scale(parameter));
    }

    public double parameterOf(Point2 point) {
        Preconditions.requireNonNull(point, "point");
        return point.subtract(origin).dot(direction.asVector());
    }

    public Point2 closestPoint(Point2 point) {
        return pointAt(parameterOf(point));
    }

    @Override
    public boolean contains(Point2 point) {
        Preconditions.requireNonNull(point, "point");
        Vector2 offset = point.subtract(origin);
        double projected = offset.dot(direction.asVector());
        Point2 nearest = pointAt(projected);
        return nearest.subtract(point).norm() <= Epsilon.EPS;
    }
}
