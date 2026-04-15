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
public record Direction2(double x, double y) {

    /**
     * Creates a normalized direction from already-normalized components.
     */
    public Direction2 {
        Preconditions.requireFinite(x, "x");
        Preconditions.requireFinite(y, "y");
        double norm = Math.sqrt(x * x + y * y);
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
    public static Direction2 from(Vector2 vector) {
        Preconditions.requireNonNull(vector, "vector");
        double norm = vector.norm();
        if (Epsilon.isZero(norm)) {
            throw new GeometryException("cannot normalize zero-length vector");
        }
        return new Direction2(vector.x() / norm, vector.y() / norm);
    }

    /**
     * Converts this direction to a vector with unit length.
     *
     * @return equivalent unit vector
     */
    public Vector2 asVector() {
        return new Vector2(x, y);
    }

    /**
     * Returns the opposite direction.
     *
     * @return reversed direction
     */
    public Direction2 reverse() {
        return new Direction2(-x, -y);
    }

    /**
     * Computes the dot product with another direction.
     *
     * @param other second direction
     * @return dot product
     */
    public double dot(Direction2 other) {
        Preconditions.requireNonNull(other, "other");
        return x * other.x + y * other.y;
    }

    /**
     * Computes the angle between this direction and another direction.
     *
     * @param other other direction
     * @return angle in radians (0 to PI)
     */
    public double angleBetween(Direction2 other) {
        Preconditions.requireNonNull(other, "other");
        double dotProduct = dot(other);
        double cosAngle = Math.max(-1.0, Math.min(1.0, dotProduct));
        return Math.acos(cosAngle);
    }

    /**
     * Computes the signed angle from this direction to another direction.
     * Positive angle indicates counter-clockwise rotation.
     *
     * @param other other direction
     * @return signed angle in radians (-PI to PI)
     */
    public double signedAngleTo(Direction2 other) {
        Preconditions.requireNonNull(other, "other");
        double angle = Math.atan2(other.y, other.x) - Math.atan2(y, x);
        if (angle > Math.PI) {
            angle -= 2 * Math.PI;
        } else if (angle < -Math.PI) {
            angle += 2 * Math.PI;
        }
        return angle;
    }

    /**
     * Returns a direction perpendicular to this one (rotated 90 degrees counter-clockwise).
     *
     * @return perpendicular direction
     */
    public Direction2 perpendicular() {
        return new Direction2(-y, x);
    }

    /**
     * Rotates this direction by a given angle.
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
        if (!(obj instanceof Direction2 other)) {
            return false;
        }
        return Epsilon.equals(x, other.x)
                && Epsilon.equals(y, other.y);
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
        return Objects.hash(xi, yi);
    }
}
