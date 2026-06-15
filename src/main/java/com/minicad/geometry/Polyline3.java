package com.minicad.geometry;

import com.minicad.common.Epsilon;
import com.minicad.common.Preconditions;

import java.util.List;
import java.util.Objects;

/**
 * Minimal 3D polyline curve.
 *
 * @param points ordered polyline vertices
 */
/**
 * Minimal 3D polyline curve.
 *
 * @param points ordered polyline vertices
 */
public final class Polyline3 implements Curve3 {
    private final List<CartesianPoint> points;

    public Polyline3(List<CartesianPoint> points) {
        this.points = points == null ? null : java.util.List.copyOf(points);
    }

    public List<CartesianPoint> getPoints() {
        return points;
    }

    // Record-style accessor
    public List<CartesianPoint> points() { return getPoints(); }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Polyline3 that = (Polyline3) o;
        return Objects.equals(points, that.points);
    }

    @Override
    public int hashCode() {
        return Objects.hash(points);
    }

    @Override
    public String toString() {
        return "Polyline3{" + "points=" + points + "}";
    }

    @Override
    public CartesianPoint pointAt(double parameter) {
        Preconditions.requireFinite(parameter, "parameter");
        if (points == null || points.isEmpty()) {
            return CartesianPoint.origin();
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
        CartesianPoint p0 = points.get(segment);
        CartesianPoint p1 = points.get(segment + 1);
        return new CartesianPoint(
            p0.x() + localT * (p1.x() - p0.x()),
            p0.y() + localT * (p1.y() - p0.y()),
            p0.z() + localT * (p1.z() - p0.z())
        );
    }

    @Override
    public boolean contains(CartesianPoint point) {
        Preconditions.requireNonNull(point, "point");
        if (points == null || points.isEmpty()) {
            return false;
        }
        // Check if point is close to any segment
        for (int i = 0; i < points.size() - 1; i++) {
            CartesianPoint p0 = points.get(i);
            CartesianPoint p1 = points.get(i + 1);
            double dist = distanceToSegment(point, p0, p1);
            if (dist < Epsilon.get()) {
                return true;
            }
        }
        return false;
    }

    private double distanceToSegment(CartesianPoint point, CartesianPoint p0, CartesianPoint p1) {
        Vector3 v = p1.subtract(p0);
        Vector3 w = point.subtract(p0);
        double c1 = w.dot(v);
        double c2 = v.dot(v);
        if (c2 < Epsilon.get()) {
            return point.distanceTo(p0);
        }
        double b = c1 / c2;
        b = Math.max(0.0, Math.min(1.0, b));
        CartesianPoint closest = p0.add(v.scale(b));
        return point.distanceTo(closest);
    }

    @Override
    public CartesianPoint closestPointTo(CartesianPoint point) {
        Preconditions.requireNonNull(point, "point");
        if (points == null || points.isEmpty()) {
            return CartesianPoint.origin();
        }
        CartesianPoint closest = points.get(0);
        double minDist = point.distanceTo(closest);
        for (int i = 0; i < points.size() - 1; i++) {
            CartesianPoint p0 = points.get(i);
            CartesianPoint p1 = points.get(i + 1);
            Vector3 v = p1.subtract(p0);
            Vector3 w = point.subtract(p0);
            double c1 = w.dot(v);
            double c2 = v.dot(v);
            if (c2 >= Epsilon.get()) {
                double b = Math.max(0.0, Math.min(1.0, c1 / c2));
                CartesianPoint candidate = p0.add(v.scale(b));
                double dist = point.distanceTo(candidate);
                if (dist < minDist) {
                    minDist = dist;
                    closest = candidate;
                }
            }
        }
        return closest;
    }

    @Override
    public java.util.List<CartesianPoint> sample(int segments) {
        return points == null ? java.util.List.of() : points;
    }
}
