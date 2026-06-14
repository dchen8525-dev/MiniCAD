package com.minicad.geometry2d;

import com.minicad.common.Epsilon;
import com.minicad.common.GeometryException;
import com.minicad.common.Preconditions;
import java.util.Objects;

/**
 * Minimal 2D ellipse representation.
 *
 * @param center ellipse center
 * @param xDirection local x direction
 * @param semiAxis1 local x semi-axis
 * @param semiAxis2 local y semi-axis
 */
/**
 * Minimal 2D ellipse representation.
 *
 * @param center ellipse center
 * @param xDirection local x direction
 * @param semiAxis1 local x semi-axis
 * @param semiAxis2 local y semi-axis
 */
public final class Ellipse2 implements Curve2 {
    private final Point2 center;
    private final Direction2 xDirection;
    private final double semiAxis1;
    private final double semiAxis2;

    public Ellipse2(Point2 center, Direction2 xDirection, double semiAxis1, double semiAxis2) {
        this.center = center;
        this.xDirection = xDirection;
        this.semiAxis1 = semiAxis1;
        this.semiAxis2 = semiAxis2;
    }

    public Point2 getCenter() {
        return center;
    }

    public Direction2 getXDirection() {
        return xDirection;
    }

    public double getSemiAxis1() {
        return semiAxis1;
    }

    public double getSemiAxis2() {
        return semiAxis2;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Ellipse2 that = (Ellipse2) o;
        return Objects.equals(center, that.center) && Objects.equals(xDirection, that.xDirection) && semiAxis1 == that.semiAxis1 && semiAxis2 == that.semiAxis2;
    }

    @Override
    public int hashCode() {
        return Objects.hash(center, xDirection, semiAxis1, semiAxis2);
    }

    @Override
    public String toString() {
        return "Ellipse2{" + "center=" + center + "xDirection=" + xDirection + "semiAxis1=" + semiAxis1 + "semiAxis2=" + semiAxis2 + "}";
    }
}
