package com.minicad.geometry;

import com.minicad.common.GeometryException;
import com.minicad.common.Preconditions;
import java.util.Objects;

/**
 * Infinite 3D line defined by an origin and a unit direction.
 *
 * @param origin point on the line
 * @param direction unit direction of the line
 * @param parameterScale world-space distance covered by one unit of line parameter
 */
/**
 * Infinite 3D line defined by an origin and a unit direction.
 *
 * @param origin point on the line
 * @param direction unit direction of the line
 * @param parameterScale world-space distance covered by one unit of line parameter
 */
public final class Line3 implements Curve3 {
    private final CartesianPoint origin;
    private final Direction3 direction;
    private final double parameterScale;

    public Line3(CartesianPoint origin, Direction3 direction, double parameterScale) {
        this.origin = origin;
        this.direction = direction;
        this.parameterScale = parameterScale;
    }

    public CartesianPoint getOrigin() {
        return origin;
    }

    public Direction3 getDirection() {
        return direction;
    }

    public double getParameterScale() {
        return parameterScale;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Line3 that = (Line3) o;
        return Objects.equals(origin, that.origin) && Objects.equals(direction, that.direction) && parameterScale == that.parameterScale;
    }

    @Override
    public int hashCode() {
        return Objects.hash(origin, direction, parameterScale);
    }

    @Override
    public String toString() {
        return "Line3{" + "origin=" + origin + "direction=" + direction + "parameterScale=" + parameterScale + "}";
    }
}
