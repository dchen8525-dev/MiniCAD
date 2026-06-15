package com.minicad.geometry2d;

import com.minicad.common.Epsilon;
import com.minicad.common.GeometryException;
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

    /**
     * Returns true if this vector is approximately zero.
     *
     * @return true if zero
     */
    public boolean isZero() {
        return Math.abs(x) < Epsilon.get() && Math.abs(y) < Epsilon.get();
    }

    /**
     * Returns the norm (magnitude) of this vector.
     *
     * @return norm
     */
    public double norm() {
        return Math.sqrt(x * x + y * y);
    }

    /**
     * Returns the squared norm (magnitude squared) of this vector.
     *
     * @return squared norm
     */
    public double normSquared() {
        return x * x + y * y;
    }

    /**
     * Returns a normalized (unit) version of this vector.
     *
     * @return normalized vector
     */
    public Vector2 normalize() {
        double n = norm();
        if (n < Epsilon.get()) {
            throw new GeometryException("cannot normalize zero-length vector");
        }
        return new Vector2(x / n, y / n);
    }

    /**
     * Returns the dot product of this vector with another.
     *
     * @param other the other vector
     * @return dot product
     */
    public double dot(Vector2 other) {
        return x * other.x + y * other.y;
    }

    /**
     * Subtracts another vector from this one.
     *
     * @param other the other vector
     * @return difference vector
     */
    public Vector2 subtract(Vector2 other) {
        return new Vector2(x - other.x, y - other.y);
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
