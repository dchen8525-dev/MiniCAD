package com.minicad.geometry2d;

import com.minicad.common.Epsilon;
import com.minicad.common.GeometryException;
import com.minicad.common.Preconditions;

import java.util.Objects;

/**
 * Immutable unit 2D direction.
 *
 * @param x x component
 * @param y y component
 */
/**
 * Immutable unit 2D direction.
 *
 * @param x x component
 * @param y y component
 */
public final class Direction2 {
    private final double x;
    private final double y;

    public Direction2(double x, double y) {
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
     * Creates a direction from a vector, normalizing it to unit length.
     *
     * @param vector the source vector
     * @return unit direction
     */
    public static Direction2 from(Vector2 vector) {
        double length = Math.sqrt(vector.x() * vector.x() + vector.y() * vector.y());
        if (length < Epsilon.get()) {
            throw new GeometryException("cannot create direction from zero-length vector");
        }
        return new Direction2(vector.x() / length, vector.y() / length);
    }

    /**
     * Returns the X-axis direction (1, 0).
     *
     * @return X-axis direction
     */
    public static Direction2 xAxis() {
        return new Direction2(1, 0);
    }

    /**
     * Returns the Y-axis direction (0, 1).
     *
     * @return Y-axis direction
     */
    public static Direction2 yAxis() {
        return new Direction2(0, 1);
    }

    /**
     * Returns this direction as a vector.
     *
     * @return vector representation
     */
    public Vector2 asVector() {
        return new Vector2(x, y);
    }

    /**
     * Returns the perpendicular direction (rotated 90 degrees counter-clockwise).
     *
     * @return perpendicular direction
     */
    public Direction2 perpendicular() {
        return new Direction2(-y, x);
    }

    /**
     * Returns the angle between this direction and another direction.
     *
     * @param other other direction
     * @return angle in radians
     */
    public double angleBetween(Direction2 other) {
        Preconditions.requireNonNull(other, "other");
        double dotVal = x * other.x + y * other.y;
        return Math.acos(Math.max(-1.0, Math.min(1.0, dotVal)));
    }

    /**
     * Returns the signed angle from this direction to another direction.
     * Positive angle means counter-clockwise rotation.
     *
     * @param other other direction
     * @return signed angle in radians
     */
    public double signedAngleTo(Direction2 other) {
        Preconditions.requireNonNull(other, "other");
        // Use atan2(cross, dot) to get signed angle
        double crossVal = x * other.y - y * other.x;
        double dotVal = x * other.x + y * other.y;
        return Math.atan2(crossVal, dotVal);
    }

    /**
     * Returns the angle between this direction and a vector.
     *
     * @param other other vector
     * @return angle in radians
     */
    public double angleBetween(Vector2 other) {
        Preconditions.requireNonNull(other, "other");
        double otherLen = Math.sqrt(other.x() * other.x() + other.y() * other.y());
        if (otherLen < Epsilon.get()) {
            return 0.0;
        }
        double dotVal = (x * other.x() + y * other.y()) / otherLen;
        return Math.acos(Math.max(-1.0, Math.min(1.0, dotVal)));
    }

    /**
     * Returns the dot product of this direction with a vector.
     *
     * @param other other vector
     * @return dot product value
     */
    public double dot(Vector2 other) {
        Preconditions.requireNonNull(other, "other");
        return x * other.x() + y * other.y();
    }

    /**
     * Returns the dot product of this direction with another direction.
     *
     * @param other other direction
     * @return dot product value
     */
    public double dot(Direction2 other) {
        Preconditions.requireNonNull(other, "other");
        return x * other.x + y * other.y;
    }

    /**
     * Returns the cross product (scalar) with a vector.
     *
     * @param other other vector
     * @return cross product scalar
     */
    public double cross(Vector2 other) {
        Preconditions.requireNonNull(other, "other");
        return x * other.y() - y * other.x();
    }

    /**
     * Returns the negated direction.
     *
     * @return negated direction
     */
    public Direction2 negate() {
        return new Direction2(-x, -y);
    }

    /**
     * Returns the reversed direction (same as negate).
     *
     * @return reversed direction
     */
    public Direction2 reverse() {
        return negate();
    }

    /**
     * Rotates this direction by an angle (counter-clockwise).
     *
     * @param angle rotation angle in radians
     * @return rotated direction
     */
    public Direction2 rotate(double angle) {
        Preconditions.requireFinite(angle, "angle");
        double cos = Math.cos(angle);
        double sin = Math.sin(angle);
        return new Direction2(x * cos - y * sin, x * sin + y * cos);
    }

    /**
     * Returns a normalized (unit) version of this direction.
     * If this direction is already normalized, returns itself.
     *
     * @return normalized direction
     */
    public Direction2 normalize() {
        double length = Math.sqrt(x * x + y * y);
        if (length < Epsilon.get()) {
            throw new GeometryException("cannot normalize zero-length direction");
        }
        if (Math.abs(length - 1.0) < Epsilon.get()) {
            return this;
        }
        return new Direction2(x / length, y / length);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Direction2 that = (Direction2) o;
        return x == that.x && y == that.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    @Override
    public String toString() {
        return "Direction2{" + "x=" + x + "y=" + y + "}";
    }
}
