package com.minicad.geometry;

import com.minicad.common.Epsilon;
import com.minicad.common.GeometryException;
import com.minicad.common.Preconditions;
import java.util.Objects;

/**
 * Immutable 3D vector.
 *
 * @param x x component
 * @param y y component
 * @param z z component
 */
/**
 * Immutable 3D vector.
 *
 * @param x x component
 * @param y y component
 * @param z z component
 */
public final class Vector3 {
    private final double x;
    private final double y;
    private final double z;

    public Vector3(double x, double y, double z) {
        Preconditions.requireFinite(x, "x");
        Preconditions.requireFinite(y, "y");
        Preconditions.requireFinite(z, "z");
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getZ() {
        return z;
    }

    // Record-style accessors
    public double x() { return getX(); }
    public double y() { return getY(); }
    public double z() { return getZ(); }

    /**
     * Returns true if this vector is approximately zero.
     *
     * @return true if zero
     */
    public boolean isZero() {
        return Math.abs(x) < Epsilon.get() && Math.abs(y) < Epsilon.get() && Math.abs(z) < Epsilon.get();
    }

    /**
     * Scales this vector by a scalar.
     *
     * @param factor the scaling factor
     * @return scaled vector
     */
    public Vector3 scale(double factor) {
        return new Vector3(x * factor, y * factor, z * factor);
    }

    /**
     * Computes the cross product of this vector with another.
     *
     * @param other the other vector
     * @return cross product
     */
    public Vector3 cross(Vector3 other) {
        return new Vector3(
            y * other.z - z * other.y,
            z * other.x - x * other.z,
            x * other.y - y * other.x
        );
    }

    /**
     * Adds another vector to this one.
     *
     * @param other the other vector
     * @return sum vector
     */
    public Vector3 add(Vector3 other) {
        return new Vector3(x + other.x, y + other.y, z + other.z);
    }

    /**
     * Subtracts another vector from this one.
     *
     * @param other the other vector
     * @return difference vector
     */
    public Vector3 subtract(Vector3 other) {
        return new Vector3(x - other.x, y - other.y, z - other.z);
    }

    /**
     * Negates this vector.
     *
     * @return negated vector
     */
    public Vector3 negate() {
        return new Vector3(-x, -y, -z);
    }

    /**
     * Returns the reverse (negated) of this vector.
     * Alias for negate().
     *
     * @return reversed vector
     */
    public Vector3 reverse() {
        return negate();
    }

    /**
     * Returns the squared norm (magnitude squared) of this vector.
     *
     * @return squared norm
     */
    public double normSquared() {
        return x * x + y * y + z * z;
    }

    /**
     * Returns the norm (magnitude) of this vector.
     *
     * @return norm
     */
    public double norm() {
        return Math.sqrt(normSquared());
    }

    /**
     * Returns the dot product of this vector with another.
     *
     * @param other the other vector
     * @return dot product
     */
    public double dot(Vector3 other) {
        return x * other.x + y * other.y + z * other.z;
    }

    /**
     * Returns a normalized (unit) version of this vector.
     *
     * @return normalized vector
     */
    public Vector3 normalize() {
        double n = norm();
        if (n < Epsilon.get()) {
            throw new GeometryException("cannot normalize zero-length vector");
        }
        return new Vector3(x / n, y / n, z / n);
    }

    /**
     * Returns this vector as a Direction3 (normalized).
     *
     * @return direction
     */
    public Direction3 asDirection() {
        return Direction3.from(normalize());
    }

    /**
     * Returns this vector (identity conversion for API compatibility).
     *
     * @return this vector
     */
    public Vector3 asVector() {
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Vector3 that = (Vector3) o;
        return x == that.x && y == that.y && z == that.z;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y, z);
    }

    @Override
    public String toString() {
        return "Vector3{" + "x=" + x + "y=" + y + "z=" + z + "}";
    }
}
