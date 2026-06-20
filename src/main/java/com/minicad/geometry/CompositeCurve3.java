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
        if (segments == null || segments.isEmpty()) {
            throw new IllegalArgumentException("composite curve requires at least one segment");
        }
        this.segments = java.util.List.copyOf(segments);
    }

    public List<Curve3> getSegments() {
        return segments;
    }

    // Record-style accessor
    public List<Curve3> segments() { return getSegments(); }

    // Convenience method
    public int segmentCount() { return segments == null ? 0 : segments.size(); }

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
        Curve3 segment = segments.get(segmentIndex);
        if (segment instanceof BSplineCurve3) {
            BSplineCurve3 spline = (BSplineCurve3) segment;
            localParam = spline.startParameter() + localParam * (spline.endParameter() - spline.startParameter());
        }
        return segment.pointAt(localParam);
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
    public double parameterAt(CartesianPoint point) {
        Preconditions.requireNonNull(point, "point");
        if (segments.size() == 1 && segments.get(0) instanceof BSplineCurve3) {
            BSplineCurve3 spline = (BSplineCurve3) segments.get(0);
            double basisParameter = spline.parameterAt(point);
            return (basisParameter - spline.startParameter()) / (spline.endParameter() - spline.startParameter());
        }
        int sampleCount = 1024;
        int bestIndex = 0;
        double bestDistance = Double.POSITIVE_INFINITY;
        for (int i = 0; i <= sampleCount; i++) {
            double distance = point.distanceTo(pointAt((double) i / sampleCount));
            if (distance < bestDistance) {
                bestDistance = distance;
                bestIndex = i;
            }
        }
        return (double) bestIndex / sampleCount;
    }

    @Override
    public Vector3 tangentAt(double parameter) {
        int count = segments.size();
        double scaled = Math.max(0.0, Math.min(1.0, parameter)) * count;
        int index = Math.min((int) scaled, count - 1);
        double local = scaled - index;
        return segments.get(index).tangentAt(local);
    }

    @Override
    public java.util.List<CartesianPoint> sample(int segments) {
        java.util.List<CartesianPoint> points = new java.util.ArrayList<>();
        if (this.segments == null || this.segments.isEmpty()) {
            return java.util.List.copyOf(points);
        }
        for (int i = 0; i <= segments; i++) {
            points.add(pointAt((double) i / segments));
        }
        return java.util.List.copyOf(points);
    }

    @Override
    public double length() {
        double total = 0.0;
        for (Curve3 segment : segments) total += segment.length();
        return total;
    }
}
