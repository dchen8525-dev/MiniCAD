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
