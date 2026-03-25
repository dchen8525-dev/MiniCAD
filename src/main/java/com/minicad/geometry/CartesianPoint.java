package com.minicad.geometry;

import com.minicad.common.Preconditions;

/**
 * Immutable 3D Cartesian point.
 *
 * @param x x coordinate
 * @param y y coordinate
 * @param z z coordinate
 */
public record CartesianPoint(double x, double y, double z) {

    /**
     * Creates a point and validates its coordinates.
     *
     */
    public CartesianPoint {
        Preconditions.requireFinite(x, "x");
        Preconditions.requireFinite(y, "y");
        Preconditions.requireFinite(z, "z");
    }

    /**
     * Returns the vector from {@code other} to this point.
     *
     * @param other start point
     * @return displacement vector
     */
    public Vector3 subtract(CartesianPoint other) {
        Preconditions.requireNonNull(other, "other");
        return new Vector3(x - other.x, y - other.y, z - other.z);
    }

    /**
     * Translates this point by a vector.
     *
     * @param vector translation vector
     * @return translated point
     */
    public CartesianPoint add(Vector3 vector) {
        Preconditions.requireNonNull(vector, "vector");
        return new CartesianPoint(x + vector.x(), y + vector.y(), z + vector.z());
    }

    /**
     * Computes the Euclidean distance to another point.
     *
     * @param other target point
     * @return Euclidean distance
     */
    public double distanceTo(CartesianPoint other) {
        return subtract(other).norm();
    }
}
