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
     * Returns the parameter value corresponding to the closest point on this line.
     *
     * @param point the point to find parameter for
     * @return parameter value t where pointAt(t) is closest to the given point
     */
    public double parameterOfClosestPoint(CartesianPoint point) {
        Preconditions.requireNonNull(point, "point");
        Vector3 toPoint = point.subtract(origin);
        return toPoint.dot(direction.asVector()) / parameterScale;
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

    /**
     * Returns the length of a line segment between two parameter values.
     *
     * @param t0 start parameter
     * @param t1 end parameter
     * @return length of segment
     */
    public double length(double t0, double t1) {
        Preconditions.requireFinite(t0, "t0");
        Preconditions.requireFinite(t1, "t1");
        return Math.abs(t1 - t0) * parameterScale;
    }

    @Override
    public boolean contains(CartesianPoint point) {
        Preconditions.requireNonNull(point, "point");
        // Check if point lies on the line within epsilon
        CartesianPoint closest = closestPointTo(point);
        return point.distanceTo(closest) < Epsilon.get();
    }

    /**
     * Returns the bounding box of a line segment from t0 to t1.
     *
     * @param t0 start parameter
     * @param t1 end parameter
     * @return bounding box of the segment
     */
    public BoundingBox3 boundingBox(double t0, double t1) {
        Preconditions.requireFinite(t0, "t0");
        Preconditions.requireFinite(t1, "t1");
        CartesianPoint p0 = pointAt(t0);
        CartesianPoint p1 = pointAt(t1);
        return BoundingBox3.of(p0, p1);
    }
}
