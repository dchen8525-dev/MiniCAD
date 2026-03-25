package com.minicad.geometry;

import com.minicad.common.Epsilon;
import com.minicad.common.Preconditions;

/**
 * Immutable 3D vector.
 *
 * @param x x component
 * @param y y component
 * @param z z component
 */
public record Vector3(double x, double y, double z) {

    /**
     * Creates a vector and validates its components.
     *
     */
    public Vector3 {
        Preconditions.requireFinite(x, "x");
        Preconditions.requireFinite(y, "y");
        Preconditions.requireFinite(z, "z");
    }

    /**
     * Returns the Euclidean norm.
     *
     * @return vector length
     */
    public double norm() {
        return Math.sqrt(normSquared());
    }

    /**
     * Returns the squared Euclidean norm.
     *
     * @return squared vector length
     */
    public double normSquared() {
        return x * x + y * y + z * z;
    }

    /**
     * Adds another vector.
     *
     * @param other addend
     * @return sum vector
     */
    public Vector3 add(Vector3 other) {
        Preconditions.requireNonNull(other, "other");
        return new Vector3(x + other.x, y + other.y, z + other.z);
    }

    /**
     * Subtracts another vector.
     *
     * @param other subtrahend
     * @return difference vector
     */
    public Vector3 subtract(Vector3 other) {
        Preconditions.requireNonNull(other, "other");
        return new Vector3(x - other.x, y - other.y, z - other.z);
    }

    /**
     * Scales the vector.
     *
     * @param scalar scale factor
     * @return scaled vector
     */
    public Vector3 scale(double scalar) {
        Preconditions.requireFinite(scalar, "scalar");
        return new Vector3(x * scalar, y * scalar, z * scalar);
    }

    /**
     * Computes the dot product.
     *
     * @param other second vector
     * @return dot product
     */
    public double dot(Vector3 other) {
        Preconditions.requireNonNull(other, "other");
        return x * other.x + y * other.y + z * other.z;
    }

    /**
     * Computes the cross product.
     *
     * @param other second vector
     * @return cross product vector
     */
    public Vector3 cross(Vector3 other) {
        Preconditions.requireNonNull(other, "other");
        return new Vector3(
                y * other.z - z * other.y,
                z * other.x - x * other.z,
                x * other.y - y * other.x
        );
    }

    /**
     * Returns whether this vector is approximately zero.
     *
     * @return whether the norm is within epsilon
     */
    public boolean isZero() {
        return Epsilon.isZero(norm());
    }

    /**
     * Converts the vector to a unit direction.
     *
     * @return normalized direction
     */
    public Direction3 normalize() {
        return Direction3.from(this);
    }
}
