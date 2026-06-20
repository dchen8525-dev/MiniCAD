package com.minicad.geometry;

import com.minicad.common.Preconditions;
import java.util.Objects;

/**
 * 3D transformation matrix for coordinate transformations.
 * Represents a 4x4 transformation matrix in homogeneous coordinates.
 */
/**
 * 3D transformation matrix for coordinate transformations.
 * Represents a 4x4 transformation matrix in homogeneous coordinates.
 */
public final class Transformation3 {
    private final double m00;
    private final double m01;
    private final double m02;
    private final double m03;
    private final double m10;
    private final double m11;
    private final double m12;
    private final double m13;
    private final double m20;
    private final double m21;
    private final double m22;
    private final double m23;
    private final double m30;
    private final double m31;
    private final double m32;
    private final double m33;

    public Transformation3(double m00, double m01, double m02, double m03, double m10, double m11, double m12, double m13, double m20, double m21, double m22, double m23, double m30, double m31, double m32, double m33) {
        this.m00 = m00;
        this.m01 = m01;
        this.m02 = m02;
        this.m03 = m03;
        this.m10 = m10;
        this.m11 = m11;
        this.m12 = m12;
        this.m13 = m13;
        this.m20 = m20;
        this.m21 = m21;
        this.m22 = m22;
        this.m23 = m23;
        this.m30 = m30;
        this.m31 = m31;
        this.m32 = m32;
        this.m33 = m33;
    }

    /**
     * Returns an identity transformation (no transformation applied).
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
     * @param tx translation along X axis
     * @param ty translation along Y axis
     * @param tz translation along Z axis
     * @return translation transformation
     */
    public static Transformation3 translation(double tx, double ty, double tz) {
        return new Transformation3(
            1, 0, 0, tx,
            0, 1, 0, ty,
            0, 0, 1, tz,
            0, 0, 0, 1
        );
    }

    /**
     * Creates a uniform scale transformation.
     *
     * @param s scale factor for all axes
     * @return scale transformation
     */
    public static Transformation3 scale(double s) {
        return scale(s, s, s);
    }

    /**
     * Creates a non-uniform scale transformation.
     *
     * @param sx scale factor for X axis
     * @param sy scale factor for Y axis
     * @param sz scale factor for Z axis
     * @return scale transformation
     */
    public static Transformation3 scale(double sx, double sy, double sz) {
        return new Transformation3(
            sx, 0, 0, 0,
            0, sy, 0, 0,
            0, 0, sz, 0,
            0, 0, 0, 1
        );
    }

    /**
     * Creates a rotation around the X axis.
     *
     * @param angle rotation angle in radians
     * @return rotation transformation
     */
    public static Transformation3 rotationX(double angle) {
        double c = Math.cos(angle);
        double s = Math.sin(angle);
        return new Transformation3(
            1, 0, 0, 0,
            0, c, -s, 0,
            0, s, c, 0,
            0, 0, 0, 1
        );
    }

    /**
     * Creates a rotation around the Y axis.
     *
     * @param angle rotation angle in radians
     * @return rotation transformation
     */
    public static Transformation3 rotationY(double angle) {
        double c = Math.cos(angle);
        double s = Math.sin(angle);
        return new Transformation3(
            c, 0, s, 0,
            0, 1, 0, 0,
            -s, 0, c, 0,
            0, 0, 0, 1
        );
    }

    /**
     * Creates a rotation around the Z axis.
     *
     * @param angle rotation angle in radians
     * @return rotation transformation
     */
    public static Transformation3 rotationZ(double angle) {
        double c = Math.cos(angle);
        double s = Math.sin(angle);
        return new Transformation3(
            c, -s, 0, 0,
            s, c, 0, 0,
            0, 0, 1, 0,
            0, 0, 0, 1
        );
    }

    /**
     * Composes (multiplies) this transformation with another.
     *
     * @param other other transformation
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

    public double getM00() {
        return m00;
    }

    public double getM01() {
        return m01;
    }

    public double getM02() {
        return m02;
    }

    public double getM03() {
        return m03;
    }

    public double getM10() {
        return m10;
    }

    public double getM11() {
        return m11;
    }

    public double getM12() {
        return m12;
    }

    public double getM13() {
        return m13;
    }

    public double getM20() {
        return m20;
    }

    public double getM21() {
        return m21;
    }

    public double getM22() {
        return m22;
    }

    public double getM23() {
        return m23;
    }

    public double getM30() {
        return m30;
    }

    public double getM31() {
        return m31;
    }

    public double getM32() {
        return m32;
    }

    public double getM33() {
        return m33;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Transformation3 that = (Transformation3) o;
        return m00 == that.m00 && m01 == that.m01 && m02 == that.m02 && m03 == that.m03 && m10 == that.m10 && m11 == that.m11 && m12 == that.m12 && m13 == that.m13 && m20 == that.m20 && m21 == that.m21 && m22 == that.m22 && m23 == that.m23 && m30 == that.m30 && m31 == that.m31 && m32 == that.m32 && m33 == that.m33;
    }

    @Override
    public int hashCode() {
        return Objects.hash(m00, m01, m02, m03, m10, m11, m12, m13, m20, m21, m22, m23, m30, m31, m32, m33);
    }

    @Override
    public String toString() {
        return "Transformation3{" + "m00=" + m00 + "m01=" + m01 + "m02=" + m02 + "m03=" + m03 + "m10=" + m10 + "m11=" + m11 + "m12=" + m12 + "m13=" + m13 + "m20=" + m20 + "m21=" + m21 + "m22=" + m22 + "m23=" + m23 + "m30=" + m30 + "m31=" + m31 + "m32=" + m32 + "m33=" + m33 + "}";
    }

    /**
     * Transforms a CartesianPoint using this transformation matrix.
     *
     * @param point the point to transform
     * @return transformed point
     */
    public CartesianPoint transform(CartesianPoint point) {
        Preconditions.requireNonNull(point, "point");
        double x = point.getX();
        double y = point.getY();
        double z = point.getZ();
        double newX = m00 * x + m01 * y + m02 * z + m03;
        double newY = m10 * x + m11 * y + m12 * z + m13;
        double newZ = m20 * x + m21 * y + m22 * z + m23;
        return new CartesianPoint(newX, newY, newZ);
    }

    /**
     * Transforms a Vector3 using this transformation matrix.
     * Vector transformation does not include translation component.
     *
     * @param vector the vector to transform
     * @return transformed vector
     */
    public Vector3 transform(Vector3 vector) {
        Preconditions.requireNonNull(vector, "vector");
        double x = vector.getX();
        double y = vector.getY();
        double z = vector.getZ();
        double newX = m00 * x + m01 * y + m02 * z;
        double newY = m10 * x + m11 * y + m12 * z;
        double newZ = m20 * x + m21 * y + m22 * z;
        return new Vector3(newX, newY, newZ);
    }

    /**
     * Transforms a Direction3 using this transformation matrix.
     * Direction transformation does not include translation component.
     *
     * @param direction the direction to transform
     * @return transformed direction
     */
    public Direction3 transform(Direction3 direction) {
        Preconditions.requireNonNull(direction, "direction");
        double x = direction.getX();
        double y = direction.getY();
        double z = direction.getZ();
        double newX = m00 * x + m01 * y + m02 * z;
        double newY = m10 * x + m11 * y + m12 * z;
        double newZ = m20 * x + m21 * y + m22 * z;
        return new Direction3(newX, newY, newZ);
    }

    /**
     * Returns the inverse transformation.
     *
     * @return inverse transformation
     */
    public Transformation3 inverse() {
        // For a 4x4 transformation matrix, compute the inverse
        // Assuming this is a rigid transformation (rotation + translation)
        // Inverse rotation is transpose, inverse translation is -translation
        return new Transformation3(
            m00, m10, m20, -(m00 * m03 + m10 * m13 + m20 * m23),
            m01, m11, m21, -(m01 * m03 + m11 * m13 + m21 * m23),
            m02, m12, m22, -(m02 * m03 + m12 * m13 + m22 * m23),
            m30, m31, m32, m33
        );
    }

    /**
     * Creates a transformation from an Axis2Placement3D.
     *
     * @param placement the axis placement
     * @return transformation representing the placement
     */
    public static Transformation3 from(Axis2Placement3D placement) {
        Preconditions.requireNonNull(placement, "placement");
        // Build an orthonormal frame following the STEP convention:
        // z = normalize(axis), x = normalize(project(refDir onto plane normal to z)), y = z x x
        Vector3 zDir = placement.getAxis().asVector();
        double zNorm = Math.sqrt(zDir.getX() * zDir.getX() + zDir.getY() * zDir.getY() + zDir.getZ() * zDir.getZ());
        if (zNorm < com.minicad.common.Epsilon.get()) {
            throw new com.minicad.common.GeometryException("axis direction must be non-zero");
        }
        Vector3 zUnit = new Vector3(zDir.getX() / zNorm, zDir.getY() / zNorm, zDir.getZ() / zNorm);
        Vector3 xSeed = placement.xDirection().asVector();
        // Gram-Schmidt: project xSeed onto the plane normal to zUnit
        double dotXZ = xSeed.getX() * zUnit.getX() + xSeed.getY() * zUnit.getY() + xSeed.getZ() * zUnit.getZ();
        Vector3 xProj = new Vector3(
            xSeed.getX() - zUnit.getX() * dotXZ,
            xSeed.getY() - zUnit.getY() * dotXZ,
            xSeed.getZ() - zUnit.getZ() * dotXZ
        );
        double xNorm = Math.sqrt(xProj.getX() * xProj.getX() + xProj.getY() * xProj.getY() + xProj.getZ() * xProj.getZ());
        if (xNorm < com.minicad.common.Epsilon.get()) {
            throw new com.minicad.common.GeometryException("refDirection is parallel to axis");
        }
        Vector3 xDir = new Vector3(xProj.getX() / xNorm, xProj.getY() / xNorm, xProj.getZ() / xNorm);
        // y = z cross x (already unit length since z and x are orthonormal)
        Vector3 yDir = new Vector3(
            zUnit.getY() * xDir.getZ() - zUnit.getZ() * xDir.getY(),
            zUnit.getZ() * xDir.getX() - zUnit.getX() * xDir.getZ(),
            zUnit.getX() * xDir.getY() - zUnit.getY() * xDir.getX()
        );
        CartesianPoint origin = placement.getLocation();
        return new Transformation3(
            xDir.getX(), yDir.getX(), zUnit.getX(), origin.getX(),
            xDir.getY(), yDir.getY(), zUnit.getY(), origin.getY(),
            xDir.getZ(), yDir.getZ(), zUnit.getZ(), origin.getZ(),
            0, 0, 0, 1
        );
    }

    /**
     * Returns the translation vector component of this transformation.
     *
     * @return translation vector
     */
    public Vector3 translation() {
        return new Vector3(m03, m13, m23);
    }

    /**
     * Returns a new transformation with the given point as the translation component.
     *
     * @param point the new translation point
     * @return transformation with updated translation
     */
    public Transformation3 at(CartesianPoint point) {
        Preconditions.requireNonNull(point, "point");
        return new Transformation3(
            m00, m01, m02, point.getX(),
            m10, m11, m12, point.getY(),
            m20, m21, m22, point.getZ(),
            m30, m31, m32, m33
        );
    }
}