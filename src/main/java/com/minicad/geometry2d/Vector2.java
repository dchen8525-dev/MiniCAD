package com.minicad.geometry2d;

import com.minicad.common.Epsilon;
import com.minicad.common.Preconditions;

/**
 * Immutable 2D vector.
 *
 * @param x x component
 * @param y y component
 */
public record Vector2(double x, double y) {

    public Vector2 {
        Preconditions.requireFinite(x, "x");
        Preconditions.requireFinite(y, "y");
    }

    public double norm() {
        return Math.sqrt(x * x + y * y);
    }

    public boolean isZero() {
        return Epsilon.isZero(norm());
    }

    public Vector2 add(Vector2 other) {
        Preconditions.requireNonNull(other, "other");
        return new Vector2(x + other.x, y + other.y);
    }

    public Vector2 subtract(Vector2 other) {
        Preconditions.requireNonNull(other, "other");
        return new Vector2(x - other.x, y - other.y);
    }

    public Vector2 scale(double scalar) {
        Preconditions.requireFinite(scalar, "scalar");
        return new Vector2(x * scalar, y * scalar);
    }

    public double dot(Vector2 other) {
        Preconditions.requireNonNull(other, "other");
        return x * other.x + y * other.y;
    }

    public Direction2 normalize() {
        return Direction2.from(this);
    }

    /**
     * Returns the negated vector.
     *
     * @return vector with opposite direction
     */
    public Vector2 negate() {
        return new Vector2(-x, -y);
    }

    /**
     * Returns a vector perpendicular to this vector (rotated 90 degrees counterclockwise).
     *
     * @return perpendicular vector
     */
    public Vector2 perpendicular() {
        return new Vector2(-y, x);
    }

    /**
     * Computes the cross product (scalar in 2D).
     * The result is the z-component of the 3D cross product.
     *
     * @param other other vector
     * @return cross product scalar
     */
    public double cross(Vector2 other) {
        Preconditions.requireNonNull(other, "other");
        return x * other.y - y * other.x;
    }

    /**
     * Computes the angle between this vector and another vector.
     *
     * @param other other vector
     * @return angle in radians between 0 and PI
     */
    public double angleBetween(Vector2 other) {
        Preconditions.requireNonNull(other, "other");
        double dotProduct = dot(other);
        double normsProduct = norm() * other.norm();
        if (Epsilon.isZero(normsProduct)) {
            return 0.0;
        }
        double cosAngle = dotProduct / normsProduct;
        cosAngle = Math.max(-1.0, Math.min(1.0, cosAngle));
        return Math.acos(cosAngle);
    }

    /**
     * Returns the squared norm.
     *
     * @return squared vector length
     */
    public double normSquared() {
        return x * x + y * y;
    }

    /**
     * Projects this vector onto another vector.
     *
     * @param onto target vector
     * @return projection of this onto onto
     */
    public Vector2 projectOnto(Vector2 onto) {
        Preconditions.requireNonNull(onto, "onto");
        double ontoNormSquared = onto.normSquared();
        if (ontoNormSquared <= Epsilon.EPS) {
            return new Vector2(0, 0);
        }
        double scalar = dot(onto) / ontoNormSquared;
        return onto.scale(scalar);
    }

    /**
     * Reflects this vector about a line defined by its normal.
     *
     * @param normal reflection normal
     * @return reflected vector
     */
    public Vector2 reflect(Vector2 normal) {
        Preconditions.requireNonNull(normal, "normal");
        double dotProduct = dot(normal);
        return subtract(normal.scale(2.0 * dotProduct));
    }

    /**
     * Rotates this vector by a given angle.
     *
     * @param angle rotation angle in radians
     * @return rotated vector
     */
    public Vector2 rotate(double angle) {
        Preconditions.requireFinite(angle, "angle");
        double cos = Math.cos(angle);
        double sin = Math.sin(angle);
        return new Vector2(x * cos - y * sin, x * sin + y * cos);
    }
}
