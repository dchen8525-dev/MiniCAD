package com.minicad.geometry2d;

import com.minicad.common.Preconditions;
import java.util.Objects;

/**
 * Immutable 2D point.
 *
 * @param x x coordinate
 * @param y y coordinate
 */
/**
 * Immutable 2D point.
 *
 * @param x x coordinate
 * @param y y coordinate
 */
public final class Point2 {
    private final double x;
    private final double y;

    public Point2(double x, double y) {
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
     * Adds a vector to this point.
     *
     * @param vector the vector to add
     * @return new point
     */
    public Point2 add(Vector2 vector) {
        return new Point2(x + vector.getX(), y + vector.getY());
    }

    /**
     * Subtracts a vector from this point.
     *
     * @param vector the vector to subtract
     * @return new point
     */
    public Point2 subtract(Vector2 vector) {
        return new Point2(x - vector.getX(), y - vector.getY());
    }

    /**
     * Subtracts another point from this point, yielding a vector.
     *
     * @param other the other point
     * @return vector from this point to the other
     */
    public Vector2 subtract(Point2 other) {
        return new Vector2(x - other.x, y - other.y);
    }

    /**
     * Returns the distance between this point and another point.
     *
     * @param other the other point
     * @return distance
     */
    public double distanceTo(Point2 other) {
        double dx = x - other.x;
        double dy = y - other.y;
        return Math.sqrt(dx * dx + dy * dy);
    }

    /**
     * Returns the midpoint between this point and another point.
     *
     * @param other the other point
     * @return midpoint
     */
    public Point2 midpoint(Point2 other) {
        Preconditions.requireNonNull(other, "other");
        return new Point2((x + other.x) / 2.0, (y + other.y) / 2.0);
    }

    /**
     * Interpolates between this point and another point.
     *
     * @param other the other point
     * @param t interpolation parameter (0 = this point, 1 = other point)
     * @return interpolated point
     */
    public Point2 interpolate(Point2 other, double t) {
        Preconditions.requireNonNull(other, "other");
        return new Point2(x + t * (other.x - x), y + t * (other.y - y));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Point2 that = (Point2) o;
        return x == that.x && y == that.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    @Override
    public String toString() {
        return "Point2{" + "x=" + x + "y=" + y + "}";
    }
}
