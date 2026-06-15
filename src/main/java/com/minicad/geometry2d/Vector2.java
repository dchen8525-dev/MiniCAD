package com.minicad.geometry2d;

import com.minicad.common.Epsilon;
import com.minicad.common.Preconditions;
import java.util.Objects;

/**
 * Immutable 2D vector.
 *
 * @param x x component
 * @param y y component
 */
/**
 * Immutable 2D vector.
 *
 * @param x x component
 * @param y y component
 */
public final class Vector2 {
    private final double x;
    private final double y;

    public Vector2(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    // Record-style accessors
    public double x() { return getX(); }
    public double y() { return getY(); }

    /**
     * Scales this vector by a scalar.
     *
     * @param factor the scaling factor
     * @return scaled vector
     */
    public Vector2 scale(double factor) {
        return new Vector2(x * factor, y * factor);
    }

    /**
     * Adds another vector to this one.
     *
     * @param other the other vector
     * @return sum vector
     */
    public Vector2 add(Vector2 other) {
        return new Vector2(x + other.x, y + other.y);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Vector2 that = (Vector2) o;
        return x == that.x && y == that.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    @Override
    public String toString() {
        return "Vector2{" + "x=" + x + "y=" + y + "}";
    }
}
