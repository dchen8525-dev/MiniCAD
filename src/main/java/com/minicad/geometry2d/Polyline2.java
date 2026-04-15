package com.minicad.geometry2d;

import com.minicad.common.Epsilon;
import com.minicad.common.Preconditions;

import java.util.List;

/**
 * Minimal 2D polyline curve.
 *
 * @param points ordered polyline vertices
 */
public record Polyline2(List<Point2> points) implements Curve2 {

    public Polyline2 {
        points = List.copyOf(points);
        if (points.size() < 2) {
            throw new IllegalArgumentException("polyline must contain at least two points");
        }
        for (Point2 point : points) {
            Preconditions.requireNonNull(point, "points");
        }
    }

    @Override
    public boolean contains(Point2 point) {
        Preconditions.requireNonNull(point, "point");
        for (int index = 0; index < points.size() - 1; index++) {
            if (pointOnSegment(points.get(index), points.get(index + 1), point)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Samples the polyline by returning all vertex points.
     *
     * @return list of all polyline vertices
     */
    public java.util.List<Point2> sample() {
        return points;
    }

    /**
     * Samples the polyline with interpolation between vertices.
     *
     * @param segmentsPerEdge number of segments between each pair of vertices
     * @return list of sampled points with interpolated points between vertices
     */
    public java.util.List<Point2> sample(int segmentsPerEdge) {
        java.util.List<Point2> sampled = new java.util.ArrayList<>();
        for (int i = 0; i < points.size() - 1; i++) {
            Point2 start = points.get(i);
            Point2 end = points.get(i + 1);
            for (int j = 0; j <= segmentsPerEdge; j++) {
                double t = (double) j / segmentsPerEdge;
                sampled.add(new Point2(
                        start.x() + (end.x() - start.x()) * t,
                        start.y() + (end.y() - start.y()) * t
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
    public Point2 pointAt(double parameter) {
        Preconditions.requireFinite(parameter, "parameter");
        int segments = points.size() - 1;
        double t = parameter * segments;
        int index = (int) Math.min(Math.floor(t), segments - 1);
        if (index < 0) index = 0;
        double localT = t - index;
        Point2 start = points.get(index);
        Point2 end = points.get(index + 1);
        return new Point2(
            start.x() + (end.x() - start.x()) * localT,
            start.y() + (end.y() - start.y()) * localT
        );
    }

    private static boolean pointOnSegment(Point2 start, Point2 end, Point2 point) {
        Vector2 segment = end.subtract(start);
        Vector2 offset = point.subtract(start);
        if (segment.isZero()) {
            return offset.norm() <= Epsilon.EPS;
        }
        double cross = segment.x() * offset.y() - segment.y() * offset.x();
        if (Math.abs(cross) > 1.0e-6) {
            return false;
        }
        double projection = offset.dot(segment);
        return projection >= -Epsilon.EPS && projection <= segment.dot(segment) + Epsilon.EPS;
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

    /**
     * Returns the bounding box enclosing all polyline vertices.
     *
     * @return bounding box enclosing the polyline
     */
    public BoundingBox2 boundingBox() {
        return BoundingBox2.of(points);
    }

    /**
     * Returns the tangent vector at a given parameter.
     *
     * @param parameter parameter value (0 to 1)
     * @return unit tangent vector
     */
    public Vector2 tangentAt(double parameter) {
        Preconditions.requireFinite(parameter, "parameter");
        int segments = points.size() - 1;
        double t = parameter * segments;
        int index = (int) Math.min(Math.floor(t), segments - 1);
        if (index < 0) index = 0;
        Point2 start = points.get(index);
        Point2 end = points.get(Math.min(index + 1, segments));
        Vector2 direction = end.subtract(start);
        if (direction.isZero()) {
            if (index > 0) {
                direction = points.get(index).subtract(points.get(index - 1));
            } else if (index < segments - 1) {
                direction = points.get(index + 2).subtract(points.get(index + 1));
            } else {
                return new Vector2(1, 0);
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
    public Point2 closestPointTo(Point2 point) {
        Preconditions.requireNonNull(point, "point");
        Point2 closest = points.get(0);
        double minDistance = point.distanceTo(closest);
        for (int i = 0; i < points.size() - 1; i++) {
            Point2 segmentClosest = closestPointOnSegment(points.get(i), points.get(i + 1), point);
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
    public double distanceTo(Point2 point) {
        Preconditions.requireNonNull(point, "point");
        return point.distanceTo(closestPointTo(point));
    }

    /**
     * Returns the midpoint of the polyline (by length).
     *
     * @return midpoint
     */
    public Point2 midpoint() {
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

    /**
     * Returns the number of segments.
     *
     * @return number of segments (points.size() - 1)
     */
    public int segmentCount() {
        return points.size() - 1;
    }

    /**
     * Returns the first point.
     *
     * @return first point
     */
    public Point2 startPoint() {
        return points.get(0);
    }

    /**
     * Returns the last point.
     *
     * @return last point
     */
    public Point2 endPoint() {
        return points.get(points.size() - 1);
    }

    /**
     * Returns the point count.
     *
     * @return number of points
     */
    public int pointCount() {
        return points.size();
    }

    private static Point2 closestPointOnSegment(Point2 start, Point2 end, Point2 point) {
        Vector2 segment = end.subtract(start);
        if (segment.isZero()) {
            return start;
        }
        double t = point.subtract(start).dot(segment) / segment.dot(segment);
        t = Math.max(0.0, Math.min(1.0, t));
        return start.add(segment.scale(t));
    }
}
