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
        if (points == null || points.size() < 2) {
            throw new IllegalArgumentException("polyline requires at least two points");
        }
        this.points = java.util.List.copyOf(points);
    }

    public List<Point2> getPoints() {
        return points;
    }

    // Record-style accessors
    public List<Point2> points() { return getPoints(); }

    // Convenience methods
    public int pointCount() { return points == null ? 0 : points.size(); }
    public int segmentCount() { return points == null || points.size() < 2 ? 0 : points.size() - 1; }
    public Point2 startPoint() { return points == null || points.isEmpty() ? null : points.get(0); }
    public Point2 endPoint() { return points == null || points.isEmpty() ? null : points.get(points.size() - 1); }
    public List<Point2> sample() { return sample(0); } // Returns all points

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
        return pointAtLength(Math.max(0.0, Math.min(1.0, parameter)) * length());
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
    public Point2 closestPointTo(Point2 point) {
        Preconditions.requireNonNull(point, "point");
        Point2 closest = points.get(0);
        double bestDistance = point.distanceTo(closest);
        for (int i = 0; i < points.size() - 1; i++) {
            Point2 start = points.get(i);
            Vector2 segment = points.get(i + 1).subtract(start);
            double denominator = segment.normSquared();
            double fraction = denominator <= Epsilon.get()
                ? 0.0
                : point.subtract(start).dot(segment) / denominator;
            fraction = Math.max(0.0, Math.min(1.0, fraction));
            Point2 candidate = start.add(segment.scale(fraction));
            double distance = point.distanceTo(candidate);
            if (distance < bestDistance) {
                bestDistance = distance;
                closest = candidate;
            }
        }
        return closest;
    }

    @Override
    public List<Point2> sample(int segments) {
        if (segments <= 0) {
            return points;
        }
        java.util.List<Point2> result = new java.util.ArrayList<>();
        for (int i = 0; i <= segments; i++) {
            result.add(pointAt((double) i / segments));
        }
        return List.copyOf(result);
    }

    @Override
    public double length() {
        double total = 0.0;
        for (int i = 0; i < points.size() - 1; i++) {
            total += points.get(i).distanceTo(points.get(i + 1));
        }
        return total;
    }

    @Override
    public Point2 midpoint() {
        return pointAt(0.5);
    }

    private Point2 pointAtLength(double targetLength) {
        double accumulated = 0.0;
        for (int i = 0; i < points.size() - 1; i++) {
            Point2 start = points.get(i);
            Point2 end = points.get(i + 1);
            double segmentLength = start.distanceTo(end);
            if (accumulated + segmentLength >= targetLength) {
                double fraction = segmentLength <= Epsilon.get() ? 0.0 : (targetLength - accumulated) / segmentLength;
                return start.add(end.subtract(start).scale(fraction));
            }
            accumulated += segmentLength;
        }
        return points.get(points.size() - 1);
    }
}
