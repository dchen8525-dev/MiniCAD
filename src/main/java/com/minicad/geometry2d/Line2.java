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
        List<Point2> points = new ArrayList<>();
        // Sample a finite range of the infinite line
        // Use a parameter range that scales inversely with parameterScale
        double sampleRange = 10.0; // World-space distance
        double paramRange = sampleRange / Math.max(parameterScale, 1.0e-12);
        double paramStep = 2.0 * paramRange / segments;
        for (int i = 0; i <= segments; i++) {
            double t = -paramRange + paramStep * i;
            points.add(pointAt(t));
        }
        return List.copyOf(points);
    }

    /**
     * Returns the normal vector at a parameter (perpendicular to line direction).
     * For a line, the normal is constant at all points.
     *
     * @param parameter parametric value (ignored for line)
     * @return normal vector
     */
    public Vector2 normalAt(double parameter) {
        return direction.perpendicular().asVector();
    }

    /**
     * Returns the normal vector at a segment index.
     *
     * @param segment segment index (ignored for line)
     * @return normal vector
     */
    public Vector2 normalAt(int segment) {
        return normalAt(0.0);
    }

    /**
     * Returns the curvature of the line (always 0 for a line).
     *
     * @return curvature (0)
     */
    public double curvature() {
        return 0.0;
    }

    /**
     * Returns the curvature at a parameter (always 0 for a line).
     *
     * @param parameter parametric value (ignored for line)
     * @return curvature (0)
     */
    public double curvatureAt(double parameter) {
        return curvature();
    }

    /**
     * Returns the curvature at a segment index (always 0 for a line).
     *
     * @param segment segment index (ignored for line)
     * @return curvature (0)
     */
    public double curvatureAt(int segment) {
        return curvature();
    }

    /**
     * Returns the length for one unit of parameter.
     * For Line2, this returns the parameterScale value.
     *
     * @return parameterScale (length per parameter unit)
     */
    @Override
    public double length() {
        return parameterScale;
    }

    /**
     * Returns the tangent vector at a parameter value.
     * For Line2, tangent is constant (the direction vector).
     *
     * @param parameter parameter value (ignored for line)
     * @return direction vector as tangent
     */
    @Override
    public Vector2 tangentAt(double parameter) {
        return direction.asVector();
    }

    /**
     * Returns the signed distance from a point to this line.
     * Positive distance is on the side where the normal points.
     *
     * @param point target point
     * @return signed distance
     */
    public double signedDistanceTo(Point2 point) {
        Preconditions.requireNonNull(point, "point");
        Vector2 toPoint = point.subtract(origin);
        // Signed distance uses cross product: toPoint cross direction
        // Positive when point is to the right of direction, negative when to the left
        return toPoint.cross(direction);
    }

    /**
     * Projects a point onto this line (same as closestPoint).
     *
     * @param point target point
     * @return projected point on line
     */
    public Point2 project(Point2 point) {
        return closestPoint(point);
    }

    /**
     * Returns a new line parallel to this one passing through the given point.
     *
     * @param point point on the new line
     * @return parallel line
     */
    public Line2 parallelThrough(Point2 point) {
        Preconditions.requireNonNull(point, "point");
        return new Line2(point, direction, parameterScale);
    }

    /**
     * Returns true if this line is parallel to another line.
     *
     * @param other other line
     * @return true if parallel
     */
    public boolean isParallelTo(Line2 other) {
        Preconditions.requireNonNull(other, "other");
        // Two directions are parallel if their cross product is near zero
        double cross = direction.cross(other.direction.asVector());
        return Math.abs(cross) < Epsilon.get();
    }

    /**
     * Returns a new line perpendicular to this one passing through the given point.
     *
     * @param point point on the new line
     * @return perpendicular line
     */
    public Line2 perpendicularThrough(Point2 point) {
        Preconditions.requireNonNull(point, "point");
        return new Line2(point, direction.perpendicular(), parameterScale);
    }

    /**
     * Returns the intersection point with another line (if they intersect).
     *
     * @param other other line
     * @return intersection point, or null if parallel
     */
    public Point2 intersect(Line2 other) {
        Preconditions.requireNonNull(other, "other");
        if (isParallelTo(other)) {
            return null; // Lines are parallel, no intersection
        }
        // Solve for intersection using parametric form
        Vector2 d1 = direction.asVector();
        Vector2 d2 = other.direction.asVector();
        Vector2 toOther = other.origin.subtract(origin);
        double cross = d1.cross(d2);
        double t1 = toOther.cross(d2) / cross;
        return pointAt(t1);
    }

    /**
     * Returns true if this line is coincident with another line (same line).
     *
     * @param other other line
     * @return true if coincident
     */
    public boolean isCoincidentWith(Line2 other) {
        Preconditions.requireNonNull(other, "other");
        if (!isParallelTo(other)) {
            return false;
        }
        // Check if the other origin lies on this line
        return contains(other.origin);
    }

    /**
     * Samples a segment of the line with given parameter bounds.
     *
     * @param segments number of segments
     * @param startParam start parameter
     * @param endParam end parameter
     * @return sampled points
     */
    public List<Point2> sample(int segments, double startParam, double endParam) {
        Preconditions.requireFinite(startParam, "startParam");
        Preconditions.requireFinite(endParam, "endParam");
        List<Point2> points = new ArrayList<>();
        for (int i = 0; i <= segments; i++) {
            double t = startParam + (endParam - startParam) * i / segments;
            points.add(pointAt(t));
        }
        return List.copyOf(points);
    }
}
