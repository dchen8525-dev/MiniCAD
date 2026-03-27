package com.minicad.geometry2d;

import com.minicad.common.Preconditions;

/**
 * Immutable 2D point.
 *
 * @param x x coordinate
 * @param y y coordinate
 */
public record Point2(double x, double y) {

    public Point2 {
        Preconditions.requireFinite(x, "x");
        Preconditions.requireFinite(y, "y");
    }

    public Vector2 subtract(Point2 other) {
        Preconditions.requireNonNull(other, "other");
        return new Vector2(x - other.x, y - other.y);
    }

    public Point2 add(Vector2 vector) {
        Preconditions.requireNonNull(vector, "vector");
        return new Point2(x + vector.x(), y + vector.y());
    }
}
