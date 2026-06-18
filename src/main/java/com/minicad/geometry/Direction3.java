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
public final class Direction3 {
    private final double x;
    private final double y;
    private final double z;

    public Direction3(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getZ() {
        return z;
    }

    // Record-style accessors
    public double x() { return getX(); }
    public double y() { return getY(); }
    public double z() { return getZ(); }

    /**
     * Creates a direction from a vector, normalizing it to unit length.
     *
     * @param vector the source vector
     * @return unit direction
     */
    public static Direction3 from(Vector3 vector) {
        double length = Math.sqrt(vector.x() * vector.x() + vector.y() * vector.y() + vector.z() * vector.z());
        if (length < Epsilon.get()) {
            throw new GeometryException("cannot create direction from zero-length vector");
        }
        return new Direction3(vector.x() / length, vector.y() / length, vector.z() / length);
    }

    /**
     * Returns this direction as a vector.
     *
     * @return vector representation
     */
    public Vector3 asVector() {
        return new Vector3(x, y, z);
    }

    /**
     * Returns the Z-axis direction (0, 0, 1).
     *
     * @return Z-axis direction
     */
    public static Direction3 zAxis() {
        return new Direction3(0, 0, 1);
    }

    /**
     * Returns the X-axis direction (1, 0, 0).
     *
     * @return X-axis direction
     */
    public static Direction3 xAxis() {
        return new Direction3(1, 0, 0);
    }

    /**
     * Returns the Y-axis direction (0, 1, 0).
     *
     * @return Y-axis direction
     */
    public static Direction3 yAxis() {
        return new Direction3(0, 1, 0);
    }

    /**
     * Returns this direction scaled by a factor.
     * Note: the result may not be a unit direction.
     *
     * @param factor the scaling factor
     * @return scaled direction
     */
    public Direction3 scale(double factor) {
        return new Direction3(x * factor, y * factor, z * factor);
    }

    /**
     * Returns the reverse (negated) direction.
     *
     * @return reversed direction
     */
    public Direction3 reverse() {
        return new Direction3(-x, -y, -z);
    }

    /**
     * Returns a perpendicular direction (rotated 90 degrees in the XY plane).
     *
     * @return perpendicular direction
     */
    public Direction3 perpendicular() {
        // For a direction in 3D, find a perpendicular direction
        if (Math.abs(z) < Math.abs(x)) {
            return new Direction3(-y, x, 0).normalize();
        } else {
            return new Direction3(0, -z, y).normalize();
        }
    }

    /**
     * Returns a normalized (unit) version of this direction.
     * If this direction is already normalized, returns itself.
     *
     * @return normalized direction
     */
    public Direction3 normalize() {
        double length = Math.sqrt(x * x + y * y + z * z);
        if (length < Epsilon.get()) {
            throw new GeometryException("cannot normalize zero-length direction");
        }
        if (Math.abs(length - 1.0) < Epsilon.get()) {
            return this;
        }
        return new Direction3(x / length, y / length, z / length);
    }

    /**
     * Returns the cross product of this direction with another.
     *
     * @param other other direction
     * @return cross product direction (not necessarily normalized)
     */
    public Direction3 cross(Direction3 other) {
        Preconditions.requireNonNull(other, "other");
        return new Direction3(
            y * other.z - z * other.y,
            z * other.x - x * other.z,
            x * other.y - y * other.x
        );
    }

    /**
     * Returns the dot product of this direction with another.
     *
     * @param other other direction
     * @return dot product value
     */
    public double dot(Direction3 other) {
        Preconditions.requireNonNull(other, "other");
        return x * other.x + y * other.y + z * other.z;
    }

    /**
     * Returns the dot product of this direction with a vector.
     *
     * @param vector the vector
     * @return dot product value
     */
    public double dot(Vector3 vector) {
        Preconditions.requireNonNull(vector, "vector");
        return x * vector.x() + y * vector.y() + z * vector.z();
    }

    /**
     * Returns the angle between this direction and another in radians.
     *
     * @param other other direction
     * @return angle in radians (0 to PI)
     */
    public double angleBetween(Direction3 other) {
        Preconditions.requireNonNull(other, "other");
        double dotVal = dot(other);
        // Clamp to [-1, 1] to avoid NaN from acos
        dotVal = Math.max(-1.0, Math.min(1.0, dotVal));
        return Math.acos(dotVal);
    }

    /**
     * Rotates this direction around an axis by a given angle.
     *
     * @param axis rotation axis direction
     * @param angle rotation angle in radians
     * @return rotated direction
     */
    public Direction3 rotateAround(Direction3 axis, double angle) {
        Preconditions.requireNonNull(axis, "axis");
        // Rodrigues' rotation formula
        double cosA = Math.cos(angle);
        double sinA = Math.sin(angle);
        Direction3 k = axis.normalize();
        // v_rot = v*cos(angle) + (k x v)*sin(angle) + k*(k.v)*(1-cos(angle))
        Direction3 cross = k.cross(this);
        double dotKV = k.dot(this);
        return new Direction3(
            x * cosA + cross.x * sinA + k.x * dotKV * (1 - cosA),
            y * cosA + cross.y * sinA + k.y * dotKV * (1 - cosA),
            z * cosA + cross.z * sinA + k.z * dotKV * (1 - cosA)
        ).normalize();
    }

    /**
     * Returns the signed angle between this direction and another, measured around a reference axis.
     *
     * @param other other direction
     * @param referenceAxis axis defining the sign (positive = counterclockwise around axis)
     * @return signed angle in radians (-PI to PI)
     */
    public double signedAngleBetween(Direction3 other, Direction3 referenceAxis) {
        Preconditions.requireNonNull(other, "other");
        Preconditions.requireNonNull(referenceAxis, "referenceAxis");
        double unsignedAngle = angleBetween(other);
        Direction3 cross = this.cross(other);
        // If cross product points in same direction as reference axis, angle is positive
        double sign = cross.dot(referenceAxis) >= 0 ? 1.0 : -1.0;
        return sign * unsignedAngle;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Direction3 that = (Direction3) o;
        return x == that.x && y == that.y && z == that.z;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y, z);
    }

    @Override
    public String toString() {
        return "Direction3{" + "x=" + x + "y=" + y + "z=" + z + "}";
    }
}
