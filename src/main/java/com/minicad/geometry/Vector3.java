package com.minicad.geometry;

import com.minicad.common.Epsilon;
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
