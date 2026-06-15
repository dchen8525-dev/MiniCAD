package com.minicad.geometry;

import com.minicad.common.Epsilon;
import com.minicad.common.GeometryException;
import com.minicad.common.Preconditions;
import java.util.Objects;

/**
 * Minimal 3D ellipse representation.
 *
 * @param position ellipse placement
 * @param semiAxis1 semi-axis along local X
 * @param semiAxis2 semi-axis along local Y
 */
/**
 * Minimal 3D ellipse representation.
 *
 * @param position ellipse placement
 * @param semiAxis1 semi-axis along local X
 * @param semiAxis2 semi-axis along local Y
 */
public final class Ellipse3 implements Curve3 {
    private final Axis2Placement3D position;
    private final double semiAxis1;
    private final double semiAxis2;

    public Ellipse3(Axis2Placement3D position, double semiAxis1, double semiAxis2) {
        this.position = position;
        this.semiAxis1 = semiAxis1;
        this.semiAxis2 = semiAxis2;
    }

    public Axis2Placement3D getPosition() {
        return position;
    }

    public double getSemiAxis1() {
        return semiAxis1;
    }

    public double getSemiAxis2() {
        return semiAxis2;
    }

    // Record-style accessors
    public Axis2Placement3D position() { return getPosition(); }
    public double semiAxis1() { return getSemiAxis1(); }
    public double semiAxis2() { return getSemiAxis2(); }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Ellipse3 that = (Ellipse3) o;
        return Objects.equals(position, that.position) && semiAxis1 == that.semiAxis1 && semiAxis2 == that.semiAxis2;
    }

    @Override
    public int hashCode() {
        return Objects.hash(position, semiAxis1, semiAxis2);
    }

    @Override
    public String toString() {
        return "Ellipse3{" + "position=" + position + "semiAxis1=" + semiAxis1 + "semiAxis2=" + semiAxis2 + "}";
    }
}
