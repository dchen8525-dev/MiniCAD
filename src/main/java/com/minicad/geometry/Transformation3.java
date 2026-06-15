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
        double x = point.x();
        double y = point.y();
        double z = point.z();
        double newX = m00 * x + m01 * y + m02 * z + m03;
        double newY = m10 * x + m11 * y + m12 * z + m13;
        double newZ = m20 * x + m21 * y + m22 * z + m23;
        return new CartesianPoint(newX, newY, newZ);
    }
}