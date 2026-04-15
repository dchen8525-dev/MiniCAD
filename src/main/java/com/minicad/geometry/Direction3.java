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
     * Computes the cross product with another direction.
     *
     * @param other second direction
     * @return cross product vector (not necessarily unit length)
     */
    public Vector3 cross(Direction3 other) {
        Preconditions.requireNonNull(other, "other");
        return new Vector3(
            y * other.z - z * other.y,
            z * other.x - x * other.z,
            x * other.y - y * other.x
        );
    }

    /**
     * Computes the angle between this direction and another direction.
     *
     * @param other other direction
     * @return angle in radians (0 to PI)
     */
    public double angleBetween(Direction3 other) {
        Preconditions.requireNonNull(other, "other");
        double dotProduct = dot(other);
        double cosAngle = Math.max(-1.0, Math.min(1.0, dotProduct));
        return Math.acos(cosAngle);
    }

    /**
     * Returns a direction perpendicular to this one.
     * If this direction is along Z axis, returns X direction.
     *
     * @return perpendicular direction
     */
    public Direction3 perpendicular() {
        if (Math.abs(x) < 0.9) {
            return Direction3.from(cross(new Direction3(1, 0, 0)));
        } else if (Math.abs(y) < 0.9) {
            return Direction3.from(cross(new Direction3(0, 1, 0)));
        }
        return Direction3.from(cross(new Direction3(0, 0, 1)));
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

    /**
     * Returns a direction rotated around an axis by an angle.
     *
     * @param axis rotation axis
     * @param angle rotation angle in radians
     * @return rotated direction
     */
    public Direction3 rotateAround(Direction3 axis, double angle) {
        Preconditions.requireNonNull(axis, "axis");
        Preconditions.requireFinite(angle, "angle");
        Vector3 k = axis.asVector();
        Vector3 v = asVector();
        // Rodrigues' rotation formula
        Vector3 cosTerm = v.scale(Math.cos(angle));
        Vector3 sinTerm = k.cross(v).scale(Math.sin(angle));
        Vector3 dotTerm = k.scale(k.dot(v) * (1 - Math.cos(angle)));
        return from(cosTerm.add(sinTerm).add(dotTerm));
    }

    /**
     * Returns the signed angle between this direction and another direction.
     *
     * @param other other direction
     * @param reference reference direction for determining sign
     * @return signed angle in radians
     */
    public double signedAngleBetween(Direction3 other, Direction3 reference) {
        Preconditions.requireNonNull(other, "other");
        Preconditions.requireNonNull(reference, "reference");
        double angle = angleBetween(other);
        Vector3 crossProduct = cross(other);
        if (crossProduct.dot(reference.asVector()) < 0) {
            return -angle;
        }
        return angle;
    }

    /**
     * Returns a unit direction along the X axis.
     *
     * @return (1, 0, 0)
     */
    public static Direction3 xAxis() {
        return new Direction3(1, 0, 0);
    }

    /**
     * Returns a unit direction along the Y axis.
     *
     * @return (0, 1, 0)
     */
    public static Direction3 yAxis() {
        return new Direction3(0, 1, 0);
    }

    /**
     * Returns a unit direction along the Z axis.
     *
     * @return (0, 0, 1)
     */
    public static Direction3 zAxis() {
        return new Direction3(0, 0, 1);
    }
}
