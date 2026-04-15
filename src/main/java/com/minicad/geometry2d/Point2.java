package com.minicad.geometry2d;

import com.minicad.common.Preconditions;

/**
 * Immutable 2D point.
 *
 * @param x x coordinate
 * @param y y coordinate
 */
public record Point2(double x, double y) {

    public Point2 {
        Preconditions.requireFinite(x, "x");
        Preconditions.requireFinite(y, "y");
    }

    public Vector2 subtract(Point2 other) {
        Preconditions.requireNonNull(other, "other");
        return new Vector2(x - other.x, y - other.y);
    }

    public Point2 add(Vector2 vector) {
        Preconditions.requireNonNull(vector, "vector");
        return new Point2(x + vector.x(), y + vector.y());
    }

    /**
     * Subtracts a vector from this point.
     *
     * @param vector vector to subtract
     * @return new point displaced by the vector
     */
    public Point2 subtract(Vector2 vector) {
        Preconditions.requireNonNull(vector, "vector");
        return new Point2(x - vector.x(), y - vector.y());
    }

    /**
     * Computes the Euclidean distance to another point.
     *
     * @param other target point
     * @return Euclidean distance
     */
    public double distanceTo(Point2 other) {
        Preconditions.requireNonNull(other, "other");
        return subtract(other).norm();
    }

    /**
     * Returns the midpoint between this point and another point.
     *
     * @param other other point
     * @return midpoint
     */
    public Point2 midpoint(Point2 other) {
        Preconditions.requireNonNull(other, "other");
        return new Point2((x + other.x) / 2.0, (y + other.y) / 2.0);
    }

    /**
     * Interpolates between this point and another point.
     *
     * @param other target point
     * @param t interpolation parameter (0 = this point, 1 = other point)
     * @return interpolated point
     */
    public Point2 interpolate(Point2 other, double t) {
        Preconditions.requireNonNull(other, "other");
        Preconditions.requireFinite(t, "t");
        return new Point2(x + (other.x - x) * t, y + (other.y - y) * t);
    }
}
