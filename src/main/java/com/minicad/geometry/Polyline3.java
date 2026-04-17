package com.minicad.geometry;

import com.minicad.common.Epsilon;
import com.minicad.common.Preconditions;

import java.util.List;

/**
 * Minimal 3D polyline curve.
 *
 * @param points ordered polyline vertices
 */
public record Polyline3(List<CartesianPoint> points) implements Curve3 {

    public Polyline3 {
        points = List.copyOf(points);
        if (points.size() < 2) {
            throw new IllegalArgumentException("polyline must contain at least two points");
        }
        for (CartesianPoint point : points) {
            Preconditions.requireNonNull(point, "points");
        }
    }

    @Override
    public boolean contains(CartesianPoint point) {
        Preconditions.requireNonNull(point, "point");
        for (int index = 0; index < points.size() - 1; index++) {
            if (pointOnSegment(points.get(index), points.get(index + 1), point)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Returns all polyline vertices as the sample.
     *
     * @return list of all polyline vertices
     */
    public java.util.List<CartesianPoint> sample() {
        return points;
    }

    /**
     * Samples the polyline with interpolation between vertices.
     *
     * @param segmentsPerEdge number of segments between each pair of vertices
     * @return list of sampled points with interpolated points between vertices
     */
    public java.util.List<CartesianPoint> sample(int segmentsPerEdge) {
        java.util.List<CartesianPoint> sampled = new java.util.ArrayList<>();
        for (int i = 0; i < points.size() - 1; i++) {
            CartesianPoint start = points.get(i);
            CartesianPoint end = points.get(i + 1);
            for (int j = 0; j <= segmentsPerEdge; j++) {
                double t = (double) j / segmentsPerEdge;
                sampled.add(new CartesianPoint(
                        start.x() + (end.x() - start.x()) * t,
                        start.y() + (end.y() - start.y()) * t,
                        start.z() + (end.z() - start.z()) * t
                ));
            }
        }
        return java.util.List.copyOf(sampled);
    }

    /**
     * Evaluates a point on the polyline at the given parameter.
     * The parameter is mapped along the entire polyline length.
     *
     * @param parameter parameter value (0 to 1)
     * @return point on the polyline
     */
    @Override
    public CartesianPoint pointAt(double parameter) {
        Preconditions.requireFinite(parameter, "parameter");
        int segments = points.size() - 1;
        double t = parameter * segments;
        int index = (int) Math.min(Math.floor(t), segments - 1);
        if (index < 0) index = 0;
        double localT = t - index;
        CartesianPoint start = points.get(index);
        CartesianPoint end = points.get(index + 1);
        return new CartesianPoint(
            start.x() + (end.x() - start.x()) * localT,
            start.y() + (end.y() - start.y()) * localT,
            start.z() + (end.z() - start.z()) * localT
        );
    }

    /**
     * Returns the bounding box enclosing all polyline vertices.
     *
     * @return bounding box enclosing the polyline
     */
    public BoundingBox3 boundingBox() {
        BoundingBox3 box = BoundingBox3.empty();
        for (CartesianPoint point : points) {
            box = box.union(point);
        }
        return box;
    }

    /**
     * Returns the total length of the polyline.
     *
     * @return total length
     */
    public double length() {
        double totalLength = 0.0;
        for (int i = 0; i < points.size() - 1; i++) {
            totalLength += points.get(i).distanceTo(points.get(i + 1));
        }
        return totalLength;
    }

    private static boolean pointOnSegment(CartesianPoint start, CartesianPoint end, CartesianPoint point) {
        Vector3 segment = end.subtract(start);
        Vector3 offset = point.subtract(start);
        if (segment.isZero()) {
            return point.distanceTo(start) <= Epsilon.EPS;
        }
        if (segment.cross(offset).norm() > 1.0e-6) {
            return false;
        }
        double projection = offset.dot(segment);
        return projection >= -Epsilon.EPS && projection <= segment.dot(segment) + Epsilon.EPS;
    }

    /**
     * Returns the tangent vector at a given parameter.
     * The tangent is computed from the segment containing the parameter.
     *
     * @param parameter parameter value (0 to 1)
     * @return unit tangent vector
     */
    public Vector3 tangentAt(double parameter) {
        Preconditions.requireFinite(parameter, "parameter");
        int segments = points.size() - 1;
        double t = parameter * segments;
        int index = (int) Math.min(Math.floor(t), segments - 1);
        if (index < 0) index = 0;
        CartesianPoint start = points.get(index);
        CartesianPoint end = points.get(Math.min(index + 1, segments));
        Vector3 direction = end.subtract(start);
        if (direction.norm() <= Epsilon.EPS) {
            // Handle degenerate segment - use next segment or default
            if (index > 0) {
                direction = points.get(index).subtract(points.get(index - 1));
            } else if (index < segments - 1) {
                direction = points.get(index + 2).subtract(points.get(index + 1));
            } else {
                return new Vector3(1, 0, 0);
            }
        }
        return direction.normalize().asVector();
    }

    /**
     * Returns the closest point on the polyline to a given point.
     *
     * @param point target point
     * @return closest point on the polyline
     */
    @Override
    public CartesianPoint closestPointTo(CartesianPoint point) {
        Preconditions.requireNonNull(point, "point");
        CartesianPoint closest = points.get(0);
        double minDistance = point.distanceTo(closest);
        for (int i = 0; i < points.size() - 1; i++) {
            CartesianPoint segmentClosest = closestPointOnSegment(points.get(i), points.get(i + 1), point);
            double distance = point.distanceTo(segmentClosest);
            if (distance < minDistance) {
                minDistance = distance;
                closest = segmentClosest;
            }
        }
        return closest;
    }

    /**
     * Returns the distance from a point to the polyline.
     *
     * @param point target point
     * @return minimum distance to the polyline
     */
    @Override
    public double distanceTo(CartesianPoint point) {
        Preconditions.requireNonNull(point, "point");
        return point.distanceTo(closestPointTo(point));
    }

    /**
     * Returns the number of segments in the polyline.
     *
     * @return number of segments (points.size() - 1)
     */
    public int segmentCount() {
        return points.size() - 1;
    }

    /**
     * Returns the first point of the polyline.
     *
     * @return first point
     */
    public CartesianPoint startPoint() {
        return points.get(0);
    }

    /**
     * Returns the last point of the polyline.
     *
     * @return last point
     */
    public CartesianPoint endPoint() {
        return points.get(points.size() - 1);
    }

    /**
     * Returns the midpoint of the polyline.
     *
     * @return midpoint
     */
    public CartesianPoint midpoint() {
        double halfLength = length() / 2.0;
        double cumulative = 0.0;
        for (int i = 0; i < points.size() - 1; i++) {
            double segmentLength = points.get(i).distanceTo(points.get(i + 1));
            if (cumulative + segmentLength >= halfLength) {
                double t = (halfLength - cumulative) / segmentLength;
                return points.get(i).interpolate(points.get(i + 1), t);
            }
            cumulative += segmentLength;
        }
        return points.get(points.size() - 1);
    }

    private static CartesianPoint closestPointOnSegment(CartesianPoint start, CartesianPoint end, CartesianPoint point) {
        Vector3 segment = end.subtract(start);
        if (segment.isZero()) {
            return start;
        }
        double t = point.subtract(start).dot(segment) / segment.dot(segment);
        t = Math.max(0.0, Math.min(1.0, t));
        return start.add(segment.scale(t));
    }
}
