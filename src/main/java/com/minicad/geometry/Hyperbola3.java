package com.minicad.geometry;

import com.minicad.common.Epsilon;
import com.minicad.common.GeometryException;
import com.minicad.common.Preconditions;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Minimal 3D hyperbola representation.
 * A hyperbola is a conic section defined by two semi-axes.
 *
 * @param position hyperbola placement (center at origin, transverse axis along local X)
 * @param semiAxisA semi-major axis (transverse axis)
 * @param semiAxisB semi-minor axis (conjugate axis)
 */
/**
 * Minimal 3D hyperbola representation.
 * A hyperbola is a conic section defined by two semi-axes.
 *
 * @param position hyperbola placement (center at origin, transverse axis along local X)
 * @param semiAxisA semi-major axis (transverse axis)
 * @param semiAxisB semi-minor axis (conjugate axis)
 */
public final class Hyperbola3 implements Curve3 {
    private final Axis2Placement3D position;
    private final double semiAxisA;
    private final double semiAxisB;

    public Hyperbola3(Axis2Placement3D position, double semiAxisA, double semiAxisB) {
        this.position = position;
        this.semiAxisA = semiAxisA;
        this.semiAxisB = semiAxisB;
    }

    public Axis2Placement3D getPosition() {
        return position;
    }

    public double getSemiAxisA() {
        return semiAxisA;
    }

    public double getSemiAxisB() {
        return semiAxisB;
    }

    // Record-style accessors
    public Axis2Placement3D position() { return position; }
    public double semiAxisA() { return semiAxisA; }
    public double semiAxisB() { return semiAxisB; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Hyperbola3 that = (Hyperbola3) o;
        return Objects.equals(position, that.position) && semiAxisA == that.semiAxisA && semiAxisB == that.semiAxisB;
    }

    @Override
    public int hashCode() {
        return Objects.hash(position, semiAxisA, semiAxisB);
    }

    @Override
    public String toString() {
        return "Hyperbola3{" + "position=" + position + "semiAxisA=" + semiAxisA + "semiAxisB=" + semiAxisB + "}";
    }
}