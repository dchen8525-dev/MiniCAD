package com.minicad.geometry2d;

import com.minicad.common.Epsilon;
import com.minicad.common.GeometryException;
import com.minicad.common.Preconditions;
import java.util.Objects;

/**
 * Minimal 2D circle representation.
 *
 * @param center circle center
 * @param xDirection local x direction
 * @param radius positive radius
 */
/**
 * Minimal 2D circle representation.
 *
 * @param center circle center
 * @param xDirection local x direction
 * @param radius positive radius
 */
public final class Circle2 implements Curve2 {
    private final Point2 center;
    private final Direction2 xDirection;
    private final double radius;

    public Circle2(Point2 center, Direction2 xDirection, double radius) {
        this.center = center;
        this.xDirection = xDirection;
        this.radius = radius;
    }

    public Point2 getCenter() {
        return center;
    }

    public Direction2 getXDirection() {
        return xDirection;
    }

    public double getRadius() {
        return radius;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Circle2 that = (Circle2) o;
        return Objects.equals(center, that.center) && Objects.equals(xDirection, that.xDirection) && radius == that.radius;
    }

    @Override
    public int hashCode() {
        return Objects.hash(center, xDirection, radius);
    }

    @Override
    public String toString() {
        return "Circle2{" + "center=" + center + "xDirection=" + xDirection + "radius=" + radius + "}";
    }
}
