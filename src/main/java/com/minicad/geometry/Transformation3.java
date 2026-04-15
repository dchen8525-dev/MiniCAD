package com.minicad.geometry;

import com.minicad.common.Preconditions;

/**
 * 3D transformation matrix for coordinate transformations.
 * Represents a 4x4 transformation matrix in homogeneous coordinates.
 */
public record Transformation3(
    double m00, double m01, double m02, double m03,
    double m10, double m11, double m12, double m13,
    double m20, double m21, double m22, double m23,
    double m30, double m31, double m32, double m33
) {

    /**
     * Creates an identity transformation.
     *
     * @return identity transformation
     */
    public static Transformation3 identity() {
        return new Transformation3(
            1, 0, 0, 0,
            0, 1, 0, 0,
            0, 0, 1, 0,
            0, 0, 0, 1
        );
    }

    /**
     * Creates a translation transformation.
     *
     * @param tx translation along X
     * @param ty translation along Y
     * @param tz translation along Z
     * @return translation transformation
     */
    public static Transformation3 translation(double tx, double ty, double tz) {
        Preconditions.requireFinite(tx, "tx");
        Preconditions.requireFinite(ty, "ty");
        Preconditions.requireFinite(tz, "tz");
        return new Transformation3(
            1, 0, 0, tx,
            0, 1, 0, ty,
            0, 0, 1, tz,
            0, 0, 0, 1
        );
    }

    /**
     * Creates a translation transformation from a vector.
     *
     * @param translation translation vector
     * @return translation transformation
     */
    public static Transformation3 translation(Vector3 translation) {
        Preconditions.requireNonNull(translation, "translation");
        return translation(translation.x(), translation.y(), translation.z());
    }

    /**
     * Creates a scale transformation.
     *
     * @param sx scale factor along X
     * @param sy scale factor along Y
     * @param sz scale factor along Z
     * @return scale transformation
     */
    public static Transformation3 scale(double sx, double sy, double sz) {
        Preconditions.requireFinite(sx, "sx");
        Preconditions.requireFinite(sy, "sy");
        Preconditions.requireFinite(sz, "sz");
        return new Transformation3(
            sx, 0, 0, 0,
            0, sy, 0, 0,
            0, 0, sz, 0,
            0, 0, 0, 1
        );
    }

    /**
     * Creates a uniform scale transformation.
     *
     * @param s uniform scale factor
     * @return scale transformation
     */
    public static Transformation3 scale(double s) {
        Preconditions.requireFinite(s, "s");
        return scale(s, s, s);
    }

    /**
     * Creates a rotation transformation around the X axis.
     *
     * @param angle rotation angle in radians
     * @return rotation transformation
     */
    public static Transformation3 rotationX(double angle) {
        Preconditions.requireFinite(angle, "angle");
        double cos = Math.cos(angle);
        double sin = Math.sin(angle);
        return new Transformation3(
            1, 0, 0, 0,
            0, cos, -sin, 0,
            0, sin, cos, 0,
            0, 0, 0, 1
        );
    }

    /**
     * Creates a rotation transformation around the Y axis.
     *
     * @param angle rotation angle in radians
     * @return rotation transformation
     */
    public static Transformation3 rotationY(double angle) {
        Preconditions.requireFinite(angle, "angle");
        double cos = Math.cos(angle);
        double sin = Math.sin(angle);
        return new Transformation3(
            cos, 0, sin, 0,
            0, 1, 0, 0,
            -sin, 0, cos, 0,
            0, 0, 0, 1
        );
    }

    /**
     * Creates a rotation transformation around the Z axis.
     *
     * @param angle rotation angle in radians
     * @return rotation transformation
     */
    public static Transformation3 rotationZ(double angle) {
        Preconditions.requireFinite(angle, "angle");
        double cos = Math.cos(angle);
        double sin = Math.sin(angle);
        return new Transformation3(
            cos, -sin, 0, 0,
            sin, cos, 0, 0,
            0, 0, 1, 0,
            0, 0, 0, 1
        );
    }

    /**
     * Creates a transformation from an Axis2Placement3D.
     *
     * @param placement axis placement
     * @return transformation matrix
     */
    public static Transformation3 from(Axis2Placement3D placement) {
        Preconditions.requireNonNull(placement, "placement");
        Direction3 x = placement.xDirection();
        Direction3 y = placement.yDirection();
        Direction3 z = placement.axis();
        CartesianPoint origin = placement.location();
        return new Transformation3(
            x.x(), y.x(), z.x(), origin.x(),
            x.y(), y.y(), z.y(), origin.y(),
            x.z(), y.z(), z.z(), origin.z(),
            0, 0, 0, 1
        );
    }

    /**
     * Composes this transformation with another transformation.
     * The result is this transformation applied first, then the other.
     *
     * @param other transformation to compose with
     * @return composed transformation
     */
    public Transformation3 compose(Transformation3 other) {
        Preconditions.requireNonNull(other, "other");
        return new Transformation3(
            m00 * other.m00 + m01 * other.m10 + m02 * other.m20 + m03 * other.m30,
            m00 * other.m01 + m01 * other.m11 + m02 * other.m21 + m03 * other.m31,
            m00 * other.m02 + m01 * other.m12 + m02 * other.m22 + m03 * other.m32,
            m00 * other.m03 + m01 * other.m13 + m02 * other.m23 + m03 * other.m33,
            m10 * other.m00 + m11 * other.m10 + m12 * other.m20 + m13 * other.m30,
            m10 * other.m01 + m11 * other.m11 + m12 * other.m21 + m13 * other.m31,
            m10 * other.m02 + m11 * other.m12 + m12 * other.m22 + m13 * other.m32,
            m10 * other.m03 + m11 * other.m13 + m12 * other.m23 + m13 * other.m33,
            m20 * other.m00 + m21 * other.m10 + m22 * other.m20 + m23 * other.m30,
            m20 * other.m01 + m21 * other.m11 + m22 * other.m21 + m23 * other.m31,
            m20 * other.m02 + m21 * other.m12 + m22 * other.m22 + m23 * other.m32,
            m20 * other.m03 + m21 * other.m13 + m22 * other.m23 + m23 * other.m33,
            m30 * other.m00 + m31 * other.m10 + m32 * other.m20 + m33 * other.m30,
            m30 * other.m01 + m31 * other.m11 + m32 * other.m21 + m33 * other.m31,
            m30 * other.m02 + m31 * other.m12 + m32 * other.m22 + m33 * other.m32,
            m30 * other.m03 + m31 * other.m13 + m32 * other.m23 + m33 * other.m33
        );
    }

    /**
     * Transforms a point.
     *
     * @param point point to transform
     * @return transformed point
     */
    public CartesianPoint transform(CartesianPoint point) {
        Preconditions.requireNonNull(point, "point");
        double x = m00 * point.x() + m01 * point.y() + m02 * point.z() + m03;
        double y = m10 * point.x() + m11 * point.y() + m12 * point.z() + m13;
        double z = m20 * point.x() + m21 * point.y() + m22 * point.z() + m23;
        return new CartesianPoint(x, y, z);
    }

    /**
     * Transforms a vector (without translation).
     *
     * @param vector vector to transform
     * @return transformed vector
     */
    public Vector3 transform(Vector3 vector) {
        Preconditions.requireNonNull(vector, "vector");
        double x = m00 * vector.x() + m01 * vector.y() + m02 * vector.z();
        double y = m10 * vector.x() + m11 * vector.y() + m12 * vector.z();
        double z = m20 * vector.x() + m21 * vector.y() + m22 * vector.z();
        return new Vector3(x, y, z);
    }

    /**
     * Transforms a direction (without translation, normalized).
     *
     * @param direction direction to transform
     * @return transformed direction
     */
    public Direction3 transform(Direction3 direction) {
        Preconditions.requireNonNull(direction, "direction");
        Vector3 transformed = transform(direction.asVector());
        return Direction3.from(transformed);
    }

    /**
     * Returns the inverse transformation.
     * Only valid for transformations where the last row is (0, 0, 0, 1).
     *
     * @return inverse transformation
     */
    public Transformation3 inverse() {
        // For affine transformations with last row (0, 0, 0, 1):
        // The inverse is computed from the rotation/scale part and translation
        double det = m00 * (m11 * m22 - m12 * m21)
                  - m01 * (m10 * m22 - m12 * m20)
                  + m02 * (m10 * m21 - m11 * m20);

        if (Math.abs(det) < 1e-12) {
            throw new IllegalStateException("Transformation is not invertible");
        }

        double invDet = 1.0 / det;

        double inv00 = (m11 * m22 - m12 * m21) * invDet;
        double inv01 = (m02 * m21 - m01 * m22) * invDet;
        double inv02 = (m01 * m12 - m02 * m11) * invDet;
        double inv10 = (m12 * m20 - m10 * m22) * invDet;
        double inv11 = (m00 * m22 - m02 * m20) * invDet;
        double inv12 = (m02 * m10 - m00 * m12) * invDet;
        double inv20 = (m10 * m21 - m11 * m20) * invDet;
        double inv21 = (m01 * m20 - m00 * m21) * invDet;
        double inv22 = (m00 * m11 - m01 * m10) * invDet;

        double inv03 = -(inv00 * m03 + inv01 * m13 + inv02 * m23);
        double inv13 = -(inv10 * m03 + inv11 * m13 + inv12 * m23);
        double inv23 = -(inv20 * m03 + inv21 * m13 + inv22 * m23);

        return new Transformation3(
            inv00, inv01, inv02, inv03,
            inv10, inv11, inv12, inv13,
            inv20, inv21, inv22, inv23,
            0, 0, 0, 1
        );
    }

    /**
     * Returns the translation component of this transformation.
     *
     * @return translation vector
     */
    public Vector3 translation() {
        return new Vector3(m03, m13, m23);
    }
}