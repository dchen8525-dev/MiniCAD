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

    /**
     * Compares this direction with another using epsilon tolerance.
     * Two directions are equal if their components differ by less than epsilon.
     *
     * @param obj the object to compare
     * @return true if the directions are equal within epsilon tolerance
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Direction3 other)) {
            return false;
        }
        return Epsilon.equals(x, other.x)
                && Epsilon.equals(y, other.y)
                && Epsilon.equals(z, other.z);
    }

    /**
     * Computes hash code based on discretized values to maintain consistency with equals.
     *
     * @return hash code
     */
    @Override
    public int hashCode() {
        // Discretize to maintain equals/hashCode contract
        long xi = Math.round(x / Epsilon.EPS);
        long yi = Math.round(y / Epsilon.EPS);
        long zi = Math.round(z / Epsilon.EPS);
        return Objects.hash(xi, yi, zi);
    }
}
