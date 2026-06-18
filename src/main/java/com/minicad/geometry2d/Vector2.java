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
     * Returns the dot product of this vector with a direction.
     *
     * @param other other direction
     * @return dot product value
     */
    public double dot(Direction2 other) {
        Preconditions.requireNonNull(other, "other");
        return x * other.x() + y * other.y();
    }

    /**
     * Returns the cross product of this vector with another vector (2D scalar cross product).
     *
     * @param other other vector
     * @return cross product scalar (z-component of 3D cross product)
     */
    public double cross(Vector2 other) {
        Preconditions.requireNonNull(other, "other");
        return x * other.y - y * other.x;
    }

    /**
     * Returns the cross product of this vector with a direction (2D scalar cross product).
     *
     * @param other other direction
     * @return cross product scalar
     */
    public double cross(Direction2 other) {
        Preconditions.requireNonNull(other, "other");
        return x * other.y() - y * other.x();
    }

    /**
     * Returns the angle between this vector and another vector.
     *
     * @param other other vector
     * @return angle in radians
     */
    public double angleBetween(Vector2 other) {
        Preconditions.requireNonNull(other, "other");
        double dotVal = dot(other);
        double lenProduct = Math.sqrt(normSquared()) * Math.sqrt(other.normSquared());
        if (lenProduct < Epsilon.get()) {
            return 0.0;
        }
        return Math.acos(Math.max(-1.0, Math.min(1.0, dotVal / lenProduct)));
    }

    /**
     * Returns the angle between this vector and a direction.
     *
     * @param other other direction
     * @return angle in radians
     */
    public double angleBetween(Direction2 other) {
        Preconditions.requireNonNull(other, "other");
        double len = Math.sqrt(normSquared());
        if (len < Epsilon.get()) {
            return 0.0;
        }
        return Math.acos(Math.max(-1.0, Math.min(1.0, dot(other) / len)));
    }

    /**
     * Returns a perpendicular vector (rotated 90 degrees counter-clockwise).
     *
     * @return perpendicular vector
     */
    public Vector2 perpendicular() {
        return new Vector2(-y, x);
    }

    /**
     * Returns the negated vector.
     *
     * @return negated vector
     */
    public Vector2 negate() {
        return new Vector2(-x, -y);
    }

    /**
     * Returns the reversed vector (same as negate).
     *
     * @return reversed vector
     */
    public Vector2 reverse() {
        return negate();
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
