package com.minicad.geometry2d;

import com.minicad.common.Epsilon;
import com.minicad.common.Preconditions;

/**
 * Infinite 2D line.
 *
 * @param origin line origin
 * @param direction line direction
 * @param parameterScale world-space distance covered by one unit of line parameter
 */
public record Line2(Point2 origin, Direction2 direction, double parameterScale) implements Curve2 {

    public Line2 {
        Preconditions.requireNonNull(origin, "origin");
        Preconditions.requireNonNull(direction, "direction");
        Preconditions.requireFinite(parameterScale, "parameterScale");
        if (parameterScale <= Epsilon.EPS) {
            throw new IllegalArgumentException("parameterScale must be greater than epsilon");
        }
    }

    public Line2(Point2 origin, Direction2 direction) {
        this(origin, direction, 1.0);
    }

    public Point2 pointAt(double parameter) {
        Preconditions.requireFinite(parameter, "parameter");
        return origin.add(direction.asVector().scale(parameter * parameterScale));
    }

    public double parameterOf(Point2 point) {
        Preconditions.requireNonNull(point, "point");
        return point.subtract(origin).dot(direction.asVector()) / parameterScale;
    }

    public Point2 closestPoint(Point2 point) {
        return pointAt(parameterOf(point));
    }

    /**
     * Returns the closest point on the line to a given point.
     *
     * @param point query point
     * @return closest point on the line
     */
    @Override
    public Point2 closestPointTo(Point2 point) {
        return closestPoint(point);
    }

    @Override
    public boolean contains(Point2 point) {
        Preconditions.requireNonNull(point, "point");
        Vector2 offset = point.subtract(origin);
        double projected = offset.dot(direction.asVector());
        Point2 nearest = pointAt(projected);
        return nearest.subtract(point).norm() <= Epsilon.EPS;
    }

    /**
     * Samples a segment of the line at discrete points.
     *
     * @param segments number of segments
     * @param start start parameter value
     * @param end end parameter value
     * @return list of sampled points
     */
    public java.util.List<Point2> sample(int segments, double start, double end) {
        java.util.List<Point2> points = new java.util.ArrayList<>(segments + 1);
        for (int i = 0; i <= segments; i++) {
            double parameter = start + (end - start) * i / segments;
            points.add(pointAt(parameter));
        }
        return java.util.List.copyOf(points);
    }

    /**
     * Samples the line from parameter 0 to 1.
     *
     * @param segments number of segments
     * @return list of sampled points
     */
    @Override
    public java.util.List<Point2> sample(int segments) {
        return sample(segments, 0.0, 1.0);
    }

    /**
     * Returns the bounding box for the default parameter range [-1, 1].
     * For an infinite line this is a 2-unit segment centered at the origin.
     *
     * @return bounding box for default visualization range
     */
    @Override
    public BoundingBox2 boundingBox() {
        Vector2 dir = direction.asVector().scale(parameterScale);
        Point2 pNeg = origin.subtract(dir);
        Point2 pPos = origin.add(dir);
        return BoundingBox2.of(pNeg, pPos);
    }

    /**
     * Returns the length of the unit parameter segment [0,1].
     * For an infinite line this is the distance between parameter 0 and 1.
     *
     * @return length (always 1.0 for the unit segment)
     */
    @Override
    public double length() {
        return parameterScale;
    }

    /**
     * Returns the tangent vector at any point on the line.
     * For a line, the tangent is constant and equal to the direction.
     *
     * @param parameter parameter value (ignored for lines)
     * @return unit tangent vector (same as direction)
     */
    public Vector2 tangentAt(double parameter) {
        return direction.asVector();
    }

    /**
     * Returns the normal vector at any point on the line.
     * The normal is perpendicular to the direction.
     *
     * @param parameter parameter value (ignored for lines)
     * @return unit normal vector (perpendicular to direction)
     */
    public Vector2 normalAt(double parameter) {
        // Rotate direction by 90 degrees
        return new Vector2(-direction.y(), direction.x());
    }

    /**
     * Returns the curvature of the line.
     * Lines have zero curvature.
     *
     * @return curvature (always 0)
     */
    public double curvature() {
        return 0.0;
    }

    /**
     * Returns the curvature at any point on the line.
     *
     * @param parameter parameter value (curvature is constant at 0)
     * @return curvature (always 0)
     */
    public double curvatureAt(double parameter) {
        return 0.0;
    }

    /**
     * Returns the distance from a point to the line.
     *
     * @param point target point
     * @return shortest distance to the line
     */
    public double distanceTo(Point2 point) {
        Preconditions.requireNonNull(point, "point");
        Point2 closest = closestPoint(point);
        return point.distanceTo(closest);
    }

    /**
     * Returns the signed distance from a point to the line.
     * Positive if the point is on the side the normal points to.
     *
     * @param point target point
     * @return signed distance to the line
     */
    public double signedDistanceTo(Point2 point) {
        Preconditions.requireNonNull(point, "point");
        Vector2 offset = point.subtract(origin);
        return offset.cross(direction.asVector());  // Signed distance in 2D
    }

    /**
     * Projects a point onto the line.
     *
     * @param point point to project
     * @return projected point on the line
     */
    public Point2 project(Point2 point) {
        Preconditions.requireNonNull(point, "point");
        return closestPoint(point);
    }

    /**
     * Returns the line parallel to this one passing through a point.
     *
     * @param point point for the parallel line
     * @return parallel line through the point
     */
    public Line2 parallelThrough(Point2 point) {
        Preconditions.requireNonNull(point, "point");
        return new Line2(point, direction, parameterScale);
    }

    /**
     * Returns a line perpendicular to this one passing through a point.
     *
     * @param point point for the perpendicular line
     * @return perpendicular line through the point
     */
    public Line2 perpendicularThrough(Point2 point) {
        Preconditions.requireNonNull(point, "point");
        Direction2 perpDir = new Direction2(-direction.y(), direction.x());
        return new Line2(point, perpDir, parameterScale);
    }

    /**
     * Computes the intersection point with another line.
     *
     * @param other other line
     * @return intersection point, or null if lines are parallel
     */
    public Point2 intersect(Line2 other) {
        Preconditions.requireNonNull(other, "other");
        // Check if parallel
        double cross = direction.asVector().cross(other.direction.asVector());
        if (Math.abs(cross) <= Epsilon.EPS) {
            return null;  // Parallel or coincident
        }
        // Compute intersection
        Vector2 diff = other.origin.subtract(origin);
        double t = diff.cross(other.direction.asVector()) / cross;
        return pointAt(t);
    }

    /**
     * Checks if two lines are parallel.
     *
     * @param other other line
     * @return true if lines are parallel
     */
    public boolean isParallelTo(Line2 other) {
        Preconditions.requireNonNull(other, "other");
        double cross = direction.asVector().cross(other.direction.asVector());
        return Math.abs(cross) <= Epsilon.EPS;
    }

    /**
     * Checks if two lines are coincident (same line).
     *
     * @param other other line
     * @return true if lines are coincident
     */
    public boolean isCoincidentWith(Line2 other) {
        Preconditions.requireNonNull(other, "other");
        if (!isParallelTo(other)) {
            return false;
        }
        return contains(other.origin);
    }
}
