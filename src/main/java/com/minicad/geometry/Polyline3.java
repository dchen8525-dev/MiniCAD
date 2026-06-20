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
        if (points == null || points.size() < 2) {
            throw new IllegalArgumentException("polyline requires at least two points");
        }
        this.points = java.util.List.copyOf(points);
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
        return pointAtLength(Math.max(0.0, Math.min(1.0, parameter)) * length());
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
        if (points == null || points.isEmpty()) {
            return java.util.List.of();
        }
        if (segments <= 0) {
            return points;
        }
        // Interpolate along each segment
        java.util.List<CartesianPoint> result = new java.util.ArrayList<>();
        int n = points.size() - 1;
        for (int i = 0; i < n; i++) {
            CartesianPoint p0 = points.get(i);
            CartesianPoint p1 = points.get(i + 1);
            for (int j = 0; j < segments; j++) {
                double t = j / (double) segments;
                result.add(new CartesianPoint(
                    p0.getX() + t * (p1.getX() - p0.getX()),
                    p0.getY() + t * (p1.getY() - p0.getY()),
                    p0.getZ() + t * (p1.getZ() - p0.getZ())
                ));
            }
        }
        result.add(points.get(points.size() - 1)); // Add last point
        return java.util.List.copyOf(result);
    }

    /**
     * Returns all vertices of the polyline.
     *
     * @return list of points
     */
    public java.util.List<CartesianPoint> sample() {
        return points == null ? java.util.List.of() : points;
    }

    /**
     * Returns the first point of the polyline.
     *
     * @return first point, or null if empty
     */
    public CartesianPoint first() {
        return points == null || points.isEmpty() ? null : points.get(0);
    }

    /**
     * Returns the last point of the polyline.
     *
     * @return last point, or null if empty
     */
    public CartesianPoint last() {
        return points == null || points.isEmpty() ? null : points.get(points.size() - 1);
    }

    /**
     * Returns the number of points in the polyline.
     *
     * @return point count
     */
    public int pointCount() {
        return points == null ? 0 : points.size();
    }

    /**
     * Returns the number of line segments in the polyline.
     *
     * @return segment count (points.size() - 1, or 0 if empty)
     */
    public int segmentCount() {
        return points == null || points.size() < 2 ? 0 : points.size() - 1;
    }

    /**
     * Returns the start point (first point) of the polyline.
     *
     * @return start point, or null if empty
     */
    public CartesianPoint startPoint() {
        return first();
    }

    /**
     * Returns the end point (last point) of the polyline.
     *
     * @return end point, or null if empty
     */
    public CartesianPoint endPoint() {
        return last();
    }

    /**
     * Returns the midpoint of the polyline at half the total length.
     *
     * @return midpoint
     */
    public CartesianPoint midpoint() {
        if (points == null || points.isEmpty()) {
            return CartesianPoint.origin();
        }
        double halfLength = length() / 2.0;
        return pointAtLength(halfLength);
    }

    /**
     * Returns the point at a given length along the polyline.
     *
     * @param targetLength length along the polyline
     * @return point at that length
     */
    private CartesianPoint pointAtLength(double targetLength) {
        if (points == null || points.size() < 2) {
            return points == null || points.isEmpty() ? CartesianPoint.origin() : points.get(0);
        }
        double accumulated = 0.0;
        for (int i = 0; i < points.size() - 1; i++) {
            CartesianPoint p0 = points.get(i);
            CartesianPoint p1 = points.get(i + 1);
            double segmentLength = p0.distanceTo(p1);
            if (accumulated + segmentLength >= targetLength) {
                double remaining = targetLength - accumulated;
                double t = remaining / segmentLength;
                return new CartesianPoint(
                    p0.getX() + t * (p1.getX() - p0.getX()),
                    p0.getY() + t * (p1.getY() - p0.getY()),
                    p0.getZ() + t * (p1.getZ() - p0.getZ())
                );
            }
            accumulated += segmentLength;
        }
        return points.get(points.size() - 1);
    }

    /**
     * Returns the total length of the polyline.
     *
     * @return total length
     */
    public double length() {
        if (points == null || points.size() < 2) {
            return 0.0;
        }
        double totalLength = 0.0;
        for (int i = 0; i < points.size() - 1; i++) {
            totalLength += points.get(i).distanceTo(points.get(i + 1));
        }
        return totalLength;
    }

    /**
     * Returns the bounding box of the polyline.
     *
     * @return bounding box
     */
    public BoundingBox3 boundingBox() {
        if (points == null || points.isEmpty()) {
            return BoundingBox3.empty();
        }
        BoundingBox3 box = BoundingBox3.empty();
        for (CartesianPoint p : points) {
            box = box.union(p);
        }
        return box;
    }
}
