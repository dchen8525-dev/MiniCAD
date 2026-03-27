package com.minicad.geometry2d;

import com.minicad.common.Epsilon;
import com.minicad.common.Preconditions;

/**
 * Immutable 2D vector.
 *
 * @param x x component
 * @param y y component
 */
public record Vector2(double x, double y) {

    public Vector2 {
        Preconditions.requireFinite(x, "x");
        Preconditions.requireFinite(y, "y");
    }

    public double norm() {
        return Math.sqrt(x * x + y * y);
    }

    public boolean isZero() {
        return Epsilon.isZero(norm());
    }

    public Vector2 add(Vector2 other) {
        Preconditions.requireNonNull(other, "other");
        return new Vector2(x + other.x, y + other.y);
    }

    public Vector2 scale(double scalar) {
        Preconditions.requireFinite(scalar, "scalar");
        return new Vector2(x * scalar, y * scalar);
    }

    public double dot(Vector2 other) {
        Preconditions.requireNonNull(other, "other");
        return x * other.x + y * other.y;
    }

    public Direction2 normalize() {
        return Direction2.from(this);
    }
}
