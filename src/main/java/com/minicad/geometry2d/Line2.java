package com.minicad.geometry2d;

import com.minicad.common.Epsilon;
import com.minicad.common.Preconditions;
import java.util.Objects;

/**
 * Infinite 2D line.
 *
 * @param origin line origin
 * @param direction line direction
 * @param parameterScale world-space distance covered by one unit of line parameter
 */
/**
 * Infinite 2D line.
 *
 * @param origin line origin
 * @param direction line direction
 * @param parameterScale world-space distance covered by one unit of line parameter
 */
public final class Line2 implements Curve2 {
    private final Point2 origin;
    private final Direction2 direction;
    private final double parameterScale;

    public Line2(Point2 origin, Direction2 direction, double parameterScale) {
        this.origin = origin;
        this.direction = direction;
        this.parameterScale = parameterScale;
    }

    public Point2 getOrigin() {
        return origin;
    }

    public Direction2 getDirection() {
        return direction;
    }

    public double getParameterScale() {
        return parameterScale;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Line2 that = (Line2) o;
        return Objects.equals(origin, that.origin) && Objects.equals(direction, that.direction) && parameterScale == that.parameterScale;
    }

    @Override
    public int hashCode() {
        return Objects.hash(origin, direction, parameterScale);
    }

    @Override
    public String toString() {
        return "Line2{" + "origin=" + origin + "direction=" + direction + "parameterScale=" + parameterScale + "}";
    }
}
