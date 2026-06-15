package com.minicad.geometry2d;

import com.minicad.common.Epsilon;
import com.minicad.common.Preconditions;

import java.util.List;
import java.util.Objects;

/**
 * Minimal 2D polyline curve.
 *
 * @param points ordered polyline vertices
 */
/**
 * Minimal 2D polyline curve.
 *
 * @param points ordered polyline vertices
 */
public final class Polyline2 implements Curve2 {
    private final List<Point2> points;

    public Polyline2(List<Point2> points) {
        this.points = points == null ? null : java.util.List.copyOf(points);
    }

    public List<Point2> getPoints() {
        return points;
    }

    // Record-style accessors
    public List<Point2> points() { return getPoints(); }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Polyline2 that = (Polyline2) o;
        return Objects.equals(points, that.points);
    }

    @Override
    public int hashCode() {
        return Objects.hash(points);
    }

    @Override
    public String toString() {
        return "Polyline2{" + "points=" + points + "}";
    }

    @Override
    public Point2 pointAt(double parameter) {
        Preconditions.requireFinite(parameter, "parameter");
        if (points == null || points.isEmpty()) {
            return new Point2(0, 0);
        }
        if (points.size() == 1) {
            return points.get(0);
        }
        // Parameter is normalized [0, 1] along the polyline
        double t = Math.max(0.0, Math.min(1.0, parameter));
        int n = points.size() - 1;
        int segment = (int) (t * n);
        segment = Math.max(0, Math.min(segment, n - 1));
        double localT = t * n - segment;
        Point2 p0 = points.get(segment);
        Point2 p1 = points.get(segment + 1);
        return new Point2(p0.x() + localT * (p1.x() - p0.x()), p0.y() + localT * (p1.y() - p0.y()));
    }

    @Override
    public boolean contains(Point2 point) {
        Preconditions.requireNonNull(point, "point");
        if (points == null || points.isEmpty()) {
            return false;
        }
        // Check if point is close to any segment
        for (int i = 0; i < points.size() - 1; i++) {
            Point2 p0 = points.get(i);
            Point2 p1 = points.get(i + 1);
            // Check distance to line segment
            double dist = distanceToSegment(point, p0, p1);
            if (dist < Epsilon.get()) {
                return true;
            }
        }
        return false;
    }

    private double distanceToSegment(Point2 point, Point2 p0, Point2 p1) {
        Vector2 v = p1.subtract(p0);
        Vector2 w = point.subtract(p0);
        double c1 = w.dot(v);
        double c2 = v.dot(v);
        if (c2 < Epsilon.get()) {
            return point.distanceTo(p0);
        }
        double b = c1 / c2;
        b = Math.max(0.0, Math.min(1.0, b));
        Point2 closest = p0.add(v.scale(b));
        return point.distanceTo(closest);
    }

    @Override
    public List<Point2> sample(int segments) {
        return points == null ? List.of() : points;
    }
}
