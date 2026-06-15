package com.minicad.geometry;

import com.minicad.common.Epsilon;
import com.minicad.common.GeometryException;
import com.minicad.common.Preconditions;

import java.util.Objects;

/**
 * Immutable unit direction in 3D.
 *
 * @param x x component
 * @param y y component
 * @param z z component
 */
public final class Direction3 {
    private final double x;
    private final double y;
    private final double z;

    public Direction3(double x, double y, double z) {
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
     * Creates a direction from a vector, normalizing it to unit length.
     *
     * @param vector the source vector
     * @return unit direction
     */
    public static Direction3 from(Vector3 vector) {
        double length = Math.sqrt(vector.x() * vector.x() + vector.y() * vector.y() + vector.z() * vector.z());
        if (length < Epsilon.get()) {
            throw new GeometryException("cannot create direction from zero-length vector");
        }
        return new Direction3(vector.x() / length, vector.y() / length, vector.z() / length);
    }

    /**
     * Returns this direction as a vector.
     *
     * @return vector representation
     */
    public Vector3 asVector() {
        return new Vector3(x, y, z);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Direction3 that = (Direction3) o;
        return x == that.x && y == that.y && z == that.z;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y, z);
    }

    @Override
    public String toString() {
        return "Direction3{" + "x=" + x + "y=" + y + "z=" + z + "}";
    }
}
