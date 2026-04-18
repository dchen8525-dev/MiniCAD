package com.minicad.geometry;

import com.minicad.common.GeometryException;
import com.minicad.common.Preconditions;

/**
 * Infinite 3D line defined by an origin and a unit direction.
 *
 * @param origin point on the line
 * @param direction unit direction of the line
 */
public record Line3(CartesianPoint origin, Direction3 direction) implements Curve3 {

    /**
     * Creates a line and validates its fields.
     *
     */
    public Line3 {
        Preconditions.requireNonNull(origin, "origin");
        Preconditions.requireNonNull(direction, "direction");
    }

    /**
     * Evaluates a point on the line.
     *
     * @param parameter signed distance along the unit direction
     * @return point at the given parameter
     */
    @Override
    public CartesianPoint pointAt(double parameter) {
        Preconditions.requireFinite(parameter, "parameter");
        return origin.add(direction.asVector().scale(parameter));
    }

    /**
     * Returns the shortest distance from a point to this line.
     *
     * @param point queried point
     * @return shortest distance
     */
    public double distanceTo(CartesianPoint point) {
        Preconditions.requireNonNull(point, "point");
        Vector3 offset = point.subtract(origin);
        Vector3 projection = direction.asVector().scale(offset.dot(direction.asVector()));
        return offset.subtract(projection).norm();
    }

    /**
     * Returns whether a point lies on the line within epsilon.
     *
     * @param point queried point
     * @return whether the point lies on the line
     */
    public boolean contains(CartesianPoint point) {
        return distanceTo(point) <= com.minicad.common.Epsilon.EPS;
    }

    /**
     * Samples a segment of the line at discrete points from parameter 0 to 1.
     *
     * @param segments number of segments
     * @return list of sampled points
     */
    @Override
    public java.util.List<CartesianPoint> sample(int segments) {
        return sample(segments, 0.0, 1.0);
    }

    /**
     * Samples a segment of the line at discrete points.
     *
     * @param segments number of segments
     * @param start start parameter value
     * @param end end parameter value
     * @return list of sampled points
     */
    public java.util.List<CartesianPoint> sample(int segments, double start, double end) {
        java.util.List<CartesianPoint> points = new java.util.ArrayList<>(segments + 1);
        for (int i = 0; i <= segments; i++) {
            double parameter = start + (end - start) * i / segments;
            points.add(pointAt(parameter));
        }
        return java.util.List.copyOf(points);
    }

    /**
     * Returns the tangent vector at any point on the line.
     * For a line, the tangent is constant and equal to the direction.
     *
     * @param parameter parameter value (ignored for lines)
     * @return unit tangent vector (same as direction)
     */
    public Vector3 tangentAt(double parameter) {
        return direction.asVector();
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
     * Computes the length of a segment of the line.
     *
     * @param start start parameter value
     * @param end end parameter value
     * @return segment length (absolute difference of parameters)
     */
    public double length(double start, double end) {
        Preconditions.requireFinite(start, "start");
        Preconditions.requireFinite(end, "end");
        return Math.abs(end - start);
    }

    @Override
    public double length() {
        // Line3 is infinite; default samples [0,1] which gives unit length.
        // Prefer length(start, end) for finite segments.
        return 1.0;
    }

    /**
     * Returns the bounding box for a finite segment of the line.
     * Infinite lines cannot have a bounded box, so this requires parameter bounds.
     *
     * @param start start parameter value
     * @param end end parameter value
     * @return bounding box for the segment
     */
    public BoundingBox3 boundingBox(double start, double end) {
        Preconditions.requireFinite(start, "start");
        Preconditions.requireFinite(end, "end");
        return BoundingBox3.of(pointAt(start), pointAt(end));
    }

    /**
     * Returns the closest point on the line to a given point.
     *
     * @param point target point
     * @return closest point on the line
     */
    @Override
    public CartesianPoint closestPointTo(CartesianPoint point) {
        Preconditions.requireNonNull(point, "point");
        return point.projectOnto(this);
    }

    /**
     * Returns the parameter value for the closest point on the line.
     *
     * @param point target point
     * @return parameter value of closest point
     */
    public double parameterOfClosestPoint(CartesianPoint point) {
        Preconditions.requireNonNull(point, "point");
        Vector3 offset = point.subtract(origin);
        return offset.dot(direction.asVector());
    }

    /**
     * Returns the curve parameter corresponding to the given point.
     * For Line3, the parameter is the signed distance along the unit direction.
     *
     * @param point a point on or near the line
     * @return signed distance parameter of the projected point
     */
    @Override
    public double parameterAt(CartesianPoint point) {
        return parameterOfClosestPoint(point);
    }
}
