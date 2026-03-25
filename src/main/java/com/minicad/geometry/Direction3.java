package com.minicad.geometry;

import com.minicad.common.Epsilon;
import com.minicad.common.GeometryException;
import com.minicad.common.Preconditions;

/**
 * Immutable unit direction in 3D.
 *
 * @param x x component
 * @param y y component
 * @param z z component
 */
public record Direction3(double x, double y, double z) {

    /**
     * Creates a normalized direction from already-normalized components.
     *
     */
    public Direction3 {
        Preconditions.requireFinite(x, "x");
        Preconditions.requireFinite(y, "y");
        Preconditions.requireFinite(z, "z");
        double norm = Math.sqrt(x * x + y * y + z * z);
        if (!Epsilon.equals(norm, 1.0)) {
            throw new GeometryException("direction must be unit length");
        }
    }

    /**
     * Creates a direction by normalizing a vector.
     *
     * @param vector source vector
     * @return unit direction
     */
    public static Direction3 from(Vector3 vector) {
        Preconditions.requireNonNull(vector, "vector");
        double norm = vector.norm();
        if (Epsilon.isZero(norm)) {
            throw new GeometryException("cannot normalize zero-length vector");
        }
        return new Direction3(vector.x() / norm, vector.y() / norm, vector.z() / norm);
    }

    /**
     * Converts this direction to a vector with unit length.
     *
     * @return equivalent unit vector
     */
    public Vector3 asVector() {
        return new Vector3(x, y, z);
    }

    /**
     * Returns the opposite direction.
     *
     * @return reversed direction
     */
    public Direction3 reverse() {
        return new Direction3(-x, -y, -z);
    }

    /**
     * Computes the dot product with another direction.
     *
     * @param other second direction
     * @return dot product
     */
    public double dot(Direction3 other) {
        Preconditions.requireNonNull(other, "other");
        return x * other.x + y * other.y + z * other.z;
    }
}
