package com.minicad.geometry;

import com.minicad.common.Epsilon;
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

    /**
     * Creates a line with default parameter scale of 1.0.
     *
     * @param origin line origin
     * @param direction line direction
     */
    public Line3(CartesianPoint origin, Direction3 direction) {
        this(origin, direction, 1.0);
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

    // Record-style accessors
    public CartesianPoint origin() { return getOrigin(); }
    public Direction3 direction() { return getDirection(); }
    public double parameterScale() { return getParameterScale(); }

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

    @Override
    public CartesianPoint pointAt(double parameter) {
        Preconditions.requireFinite(parameter, "parameter");
        Vector3 dir = direction.asVector();
        return origin.add(dir.scale(parameter * parameterScale));
    }

    @Override
    public CartesianPoint closestPointTo(CartesianPoint point) {
        Preconditions.requireNonNull(point, "point");
        // Project point onto the line
        Vector3 toPoint = point.subtract(origin);
        double t = toPoint.dot(direction.asVector()) / parameterScale;
        return pointAt(t);
    }

    /**
     * Returns the start parameter for a bounded segment.
     * Default is 0 for an unbounded line.
     *
     * @return start parameter
     */
    public double startParameter() {
        return 0.0;
    }

    /**
     * Returns the end parameter for a bounded segment.
     * Default is Double.POSITIVE_INFINITY for an unbounded line.
     *
     * @return end parameter
     */
    public double endParameter() {
        return Double.POSITIVE_INFINITY;
    }

    @Override
    public boolean contains(CartesianPoint point) {
        Preconditions.requireNonNull(point, "point");
        // Check if point lies on the line within epsilon
        CartesianPoint closest = closestPointTo(point);
        return point.distanceTo(closest) < Epsilon.get();
    }
}
