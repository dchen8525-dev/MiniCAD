package com.minicad.geometry2d;

import com.minicad.common.Epsilon;
import com.minicad.common.GeometryException;
import com.minicad.common.Preconditions;
import java.util.ArrayList;
import java.util.List;
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

    /**
     * Creates a line with default parameter scale of 1.0.
     *
     * @param origin line origin
     * @param direction line direction
     */
    public Line2(Point2 origin, Direction2 direction) {
        this(origin, direction, 1.0);
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

    // Record-style accessors
    public Point2 origin() { return getOrigin(); }
    public Direction2 direction() { return getDirection(); }
    public double parameterScale() { return getParameterScale(); }

    @Override
    public Point2 pointAt(double parameter) {
        Preconditions.requireFinite(parameter, "parameter");
        Vector2 dir = direction.asVector();
        return origin.add(dir.scale(parameter * parameterScale));
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
     * Returns the parameter corresponding to a point on the line.
     *
     * @param point a point on the line
     * @return parameter value
     */
    public double parameterOf(Point2 point) {
        Preconditions.requireNonNull(point, "point");
        Vector2 toPoint = point.subtract(origin);
        Vector2 dir = direction.asVector();
        double lengthSquared = dir.normSquared();
        if (lengthSquared < Epsilon.get()) {
            return 0.0;
        }
        return toPoint.dot(dir) / (lengthSquared * parameterScale);
    }

    /**
     * Returns the closest point on the line to a given point.
     *
     * @param point target point
     * @return closest point on the line
     */
    public Point2 closestPoint(Point2 point) {
        Preconditions.requireNonNull(point, "point");
        double t = parameterOf(point);
        return pointAt(t);
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

    @Override
    public boolean contains(Point2 point) {
        Preconditions.requireNonNull(point, "point");
        // Check if point lies on the line within epsilon
        Point2 closest = closestPoint(point);
        return point.distanceTo(closest) < Epsilon.get();
    }

    @Override
    public List<Point2> sample(int segments) {
        // For an infinite line, sample a default segment
        List<Point2> points = new ArrayList<>();
        double start = startParameter();
        double end = Math.min(endParameter(), 100.0); // Limit for sampling
        for (int i = 0; i <= segments; i++) {
            double t = start + (end - start) * i / segments;
            points.add(pointAt(t));
        }
        return List.copyOf(points);
    }
}
