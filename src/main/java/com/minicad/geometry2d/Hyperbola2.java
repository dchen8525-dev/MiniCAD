package com.minicad.geometry2d;

import com.minicad.common.Epsilon;
import com.minicad.common.GeometryException;
import com.minicad.common.Preconditions;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Minimal 2D hyperbola representation.
 * A hyperbola is a conic section defined by two semi-axes.
 *
 * @param center hyperbola center
 * @param xDirection local x direction (transverse axis direction)
 * @param semiAxisA semi-major axis (transverse axis)
 * @param semiAxisB semi-minor axis (conjugate axis)
 */
/**
 * Minimal 2D hyperbola representation.
 * A hyperbola is a conic section defined by two semi-axes.
 *
 * @param center hyperbola center
 * @param xDirection local x direction (transverse axis direction)
 * @param semiAxisA semi-major axis (transverse axis)
 * @param semiAxisB semi-minor axis (conjugate axis)
 */
public final class Hyperbola2 implements Curve2 {
    private final Point2 center;
    private final Direction2 xDirection;
    private final double semiAxisA;
    private final double semiAxisB;

    public Hyperbola2(Point2 center, Direction2 xDirection, double semiAxisA, double semiAxisB) {
        this.center = center;
        this.xDirection = xDirection;
        this.semiAxisA = semiAxisA;
        this.semiAxisB = semiAxisB;
    }

    public Point2 getCenter() {
        return center;
    }

    public Direction2 getXDirection() {
        return xDirection;
    }

    public double getSemiAxisA() {
        return semiAxisA;
    }

    public double getSemiAxisB() {
        return semiAxisB;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Hyperbola2 that = (Hyperbola2) o;
        return Objects.equals(center, that.center) && Objects.equals(xDirection, that.xDirection) && semiAxisA == that.semiAxisA && semiAxisB == that.semiAxisB;
    }

    @Override
    public int hashCode() {
        return Objects.hash(center, xDirection, semiAxisA, semiAxisB);
    }

    @Override
    public String toString() {
        return "Hyperbola2{" + "center=" + center + "xDirection=" + xDirection + "semiAxisA=" + semiAxisA + "semiAxisB=" + semiAxisB + "}";
    }
}