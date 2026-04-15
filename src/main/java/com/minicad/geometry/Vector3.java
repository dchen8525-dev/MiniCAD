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

    /**
     * Returns the negated vector (opposite direction).
     *
     * @return negated vector
     */
    public Vector3 negate() {
        return new Vector3(-x, -y, -z);
    }

    /**
     * Returns the angle between this vector and another vector.
     *
     * @param other other vector
     * @return angle in radians (0 to PI)
     */
    public double angleBetween(Vector3 other) {
        Preconditions.requireNonNull(other, "other");
        double dotProduct = dot(other);
        double norms = norm() * other.norm();
        if (norms <= Epsilon.EPS) {
            return 0.0;
        }
        double cosAngle = Math.max(-1.0, Math.min(1.0, dotProduct / norms));
        return Math.acos(cosAngle);
    }

    /**
     * Returns the angle between this vector and another vector with a reference direction.
     *
     * @param other other vector
     * @param reference reference direction for determining sign
     * @return signed angle in radians
     */
    public double signedAngleBetween(Vector3 other, Vector3 reference) {
        Preconditions.requireNonNull(other, "other");
        Preconditions.requireNonNull(reference, "reference");
        double angle = angleBetween(other);
        Vector3 crossProduct = cross(other);
        if (crossProduct.dot(reference) < 0) {
            return -angle;
        }
        return angle;
    }

    /**
     * Reflects this vector about a normal.
     *
     * @param normal reflection normal
     * @return reflected vector
     */
    public Vector3 reflect(Vector3 normal) {
        Preconditions.requireNonNull(normal, "normal");
        double dotProduct = dot(normal);
        return subtract(normal.scale(2.0 * dotProduct));
    }

    /**
     * Returns a vector perpendicular to this one.
     * If this vector is along Z axis, returns X direction.
     *
     * @return perpendicular vector
     */
    public Vector3 perpendicular() {
        // Try to find a perpendicular direction
        if (Math.abs(x) < 0.9) {
            return cross(new Vector3(1, 0, 0)).normalize().asVector();
        } else if (Math.abs(y) < 0.9) {
            return cross(new Vector3(0, 1, 0)).normalize().asVector();
        }
        return cross(new Vector3(0, 0, 1)).normalize().asVector();
    }

    /**
     * Projects this vector onto another vector.
     *
     * @param onto target vector
     * @return projection of this onto onto
     */
    public Vector3 projectOnto(Vector3 onto) {
        Preconditions.requireNonNull(onto, "onto");
        double ontoNormSquared = onto.normSquared();
        if (ontoNormSquared <= Epsilon.EPS) {
            return new Vector3(0, 0, 0);
        }
        double scalar = dot(onto) / ontoNormSquared;
        return onto.scale(scalar);
    }

    /**
     * Returns a component-wise absolute value vector.
     *
     * @return vector with absolute components
     */
    public Vector3 abs() {
        return new Vector3(Math.abs(x), Math.abs(y), Math.abs(z));
    }

    /**
     * Returns the minimum component value.
     *
     * @return minimum component
     */
    public double minComponent() {
        return Math.min(Math.min(x, y), z);
    }

    /**
     * Returns the maximum component value.
     *
     * @return maximum component
     */
    public double maxComponent() {
        return Math.max(Math.max(x, y), z);
    }

    /**
     * Returns the distance from this point to another point (treating vectors as points).
     *
     * @param other other point
     * @return Euclidean distance
     */
    public double distanceTo(Vector3 other) {
        Preconditions.requireNonNull(other, "other");
        return subtract(other).norm();
    }

    /**
     * Returns the squared distance to another point.
     *
     * @param other other point
     * @return squared distance
     */
    public double distanceSquaredTo(Vector3 other) {
        Preconditions.requireNonNull(other, "other");
        return subtract(other).normSquared();
    }

    /**
     * Returns the midpoint between this and another vector.
     *
     * @param other other vector
     * @return midpoint
     */
    public Vector3 midpoint(Vector3 other) {
        Preconditions.requireNonNull(other, "other");
        return new Vector3((x + other.x) / 2, (y + other.y) / 2, (z + other.z) / 2);
    }

    /**
     * Interpolates between this and another vector.
     *
     * @param other target vector
     * @param t interpolation parameter (0 = this, 1 = other)
     * @return interpolated vector
     */
    public Vector3 interpolate(Vector3 other, double t) {
        Preconditions.requireNonNull(other, "other");
        Preconditions.requireFinite(t, "t");
        return add(other.subtract(this).scale(t));
    }

    /**
     * Returns a vector rotated around an axis by an angle.
     *
     * @param axis rotation axis
     * @param angle rotation angle in radians
     * @return rotated vector
     */
    public Vector3 rotateAround(Vector3 axis, double angle) {
        Preconditions.requireNonNull(axis, "axis");
        Preconditions.requireFinite(angle, "angle");
        // Rodrigues' rotation formula
        Vector3 k = axis.normalize().asVector();
        Vector3 cosTerm = scale(Math.cos(angle));
        Vector3 sinTerm = k.cross(this).scale(Math.sin(angle));
        Vector3 dotTerm = k.scale(k.dot(this) * (1 - Math.cos(angle)));
        return cosTerm.add(sinTerm).add(dotTerm);
    }

    /**
     * Returns a unit vector along the X axis.
     *
     * @return (1, 0, 0)
     */
    public static Vector3 xAxis() {
        return new Vector3(1, 0, 0);
    }

    /**
     * Returns a unit vector along the Y axis.
     *
     * @return (0, 1, 0)
     */
    public static Vector3 yAxis() {
        return new Vector3(0, 1, 0);
    }

    /**
     * Returns a unit vector along the Z axis.
     *
     * @return (0, 0, 1)
     */
    public static Vector3 zAxis() {
        return new Vector3(0, 0, 1);
    }

    /**
     * Returns a zero vector.
     *
     * @return (0, 0, 0)
     */
    public static Vector3 zero() {
        return new Vector3(0, 0, 0);
    }

    /**
     * Creates a vector from an array.
     *
     * @param coords coordinate array (must have exactly 3 elements)
     * @return vector from coordinates
     */
    public static Vector3 fromArray(double[] coords) {
        Preconditions.requireNonNull(coords, "coords");
        if (coords.length != 3) {
            throw new IllegalArgumentException("coordinate array must have exactly 3 elements");
        }
        return new Vector3(coords[0], coords[1], coords[2]);
    }
}
