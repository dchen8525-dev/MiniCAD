package com.minicad.geometry;

import com.minicad.common.Epsilon;
import com.minicad.common.Preconditions;

import java.util.List;
import java.util.Objects;

/**
 * Minimal composite curve backed by multiple supported segments.
 *
 * @param segments ordered component curves
 */
/**
 * Minimal composite curve backed by multiple supported segments.
 *
 * @param segments ordered component curves
 */
public final class CompositeCurve3 implements Curve3 {
    private final List<Curve3> segments;

    public CompositeCurve3(List<Curve3> segments) {
        this.segments = segments == null ? null : java.util.List.copyOf(segments);
    }

    public List<Curve3> getSegments() {
        return segments;
    }

    // Record-style accessor
    public List<Curve3> segments() { return getSegments(); }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CompositeCurve3 that = (CompositeCurve3) o;
        return Objects.equals(segments, that.segments);
    }

    @Override
    public int hashCode() {
        return Objects.hash(segments);
    }

    @Override
    public String toString() {
        return "CompositeCurve3{" + "segments=" + segments + "}";
    }

    @Override
    public CartesianPoint pointAt(double parameter) {
        Preconditions.requireFinite(parameter, "parameter");
        if (segments == null || segments.isEmpty()) {
            return CartesianPoint.origin();
        }
        // Map parameter to segment
        int n = segments.size();
        double segmentParam = parameter * n;
        int segmentIndex = (int) segmentParam;
        segmentIndex = Math.max(0, Math.min(segmentIndex, n - 1));
        double localParam = segmentParam - segmentIndex;
        return segments.get(segmentIndex).pointAt(localParam);
    }

    @Override
    public boolean contains(CartesianPoint point) {
        Preconditions.requireNonNull(point, "point");
        if (segments == null || segments.isEmpty()) {
            return false;
        }
        for (Curve3 segment : segments) {
            if (segment.contains(point)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public CartesianPoint closestPointTo(CartesianPoint point) {
        Preconditions.requireNonNull(point, "point");
        if (segments == null || segments.isEmpty()) {
            return CartesianPoint.origin();
        }
        CartesianPoint closest = segments.get(0).closestPointTo(point);
        double minDist = point.distanceTo(closest);
        for (int i = 1; i < segments.size(); i++) {
            CartesianPoint candidate = segments.get(i).closestPointTo(point);
            double dist = point.distanceTo(candidate);
            if (dist < minDist) {
                minDist = dist;
                closest = candidate;
            }
        }
        return closest;
    }

    @Override
    public java.util.List<CartesianPoint> sample(int segments) {
        java.util.List<CartesianPoint> points = new java.util.ArrayList<>();
        if (this.segments == null || this.segments.isEmpty()) {
            return java.util.List.copyOf(points);
        }
        for (Curve3 seg : this.segments) {
            points.addAll(seg.sample(segments / this.segments.size() + 1));
        }
        return java.util.List.copyOf(points);
    }
}
