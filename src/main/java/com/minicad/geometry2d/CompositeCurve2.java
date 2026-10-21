package com.minicad.geometry2d;

import com.minicad.common.Epsilon;
import com.minicad.common.Preconditions;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Minimal composite 2D curve backed by multiple supported segments.
 *
 * @param segments ordered component curves
 */
public final class CompositeCurve2 implements Curve2 {
    private final List<Curve2> segments;

    public CompositeCurve2(List<Curve2> segments) {
        if (segments == null || segments.isEmpty()) {
            throw new IllegalArgumentException("composite curve requires at least one segment");
        }
        this.segments = java.util.List.copyOf(segments);
    }

    public List<Curve2> getSegments() {
        return segments;
    }

    // Record-style accessors
    public List<Curve2> segments() { return getSegments(); }

    /**
     * Returns the number of segments in this composite curve.
     *
     * @return segment count
     */
    public int segmentCount() {
        return segments.size();
    }

    /**
     * Returns the parameter value corresponding to a point on this curve.
     *
     * @param point the point to find parameter for
     * @return parameter value (approximate)
     */
    public double parameterOf(Point2 point) {
        Preconditions.requireNonNull(point, "point");
        // A trimmed segment can answer with the fraction along itself directly, so
        // keep that path for the accuracy it already has. It must not be derived
        // from parameterOnUnderlyingCurve: that one descends through a nested trim
        // to the innermost basis, whose parameter is not a parameter of this
        // segment and would land far outside the segment's window.
        for (int i = 0; i < segments.size(); i++) {
            Curve2 segment = segments.get(i);
            if (segment instanceof TrimmedCurve2 && segment.contains(point)) {
                TrimmedCurve2 trimmed = (TrimmedCurve2) segment;
                return (i + trimmed.trimmedParameterOf(point)) / segments.size();
            }
        }
        // Every other segment only exposes the curve's own [0, 1] parameter, which
        // is shared across the whole composite. Mirror CompositeCurve3.parameterAt:
        // take the nearest sample instead of assuming the point sits at the start
        // of whichever segment reports containment (unbounded lines all do).
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CompositeCurve2 that = (CompositeCurve2) o;
        return Objects.equals(segments, that.segments);
    }

    @Override
    public int hashCode() {
        return Objects.hash(segments);
    }

    @Override
    public String toString() {
        return "CompositeCurve2{" + "segments=" + segments + "}";
    }

    @Override
    public Point2 pointAt(double parameter) {
        Preconditions.requireFinite(parameter, "parameter");
        // Map parameter to segment
        int n = segments.size();
        double segmentParam = parameter * n;
        int segmentIndex = (int) segmentParam;
        segmentIndex = Math.max(0, Math.min(segmentIndex, n - 1));
        double localParam = segmentParam - segmentIndex;
        Curve2 segment = segments.get(segmentIndex);
        // A B-spline's natural domain is its knot range, not [0, 1]; mirror
        // CompositeCurve3.pointAt before handing the local parameter over. The
        // rational twin carries the same domain, so it needs the same remap.
        if (segment instanceof BSplineCurve2) {
            BSplineCurve2 spline = (BSplineCurve2) segment;
            localParam = spline.startParameter() + localParam * (spline.endParameter() - spline.startParameter());
        } else if (segment instanceof RationalBSplineCurve2) {
            RationalBSplineCurve2 spline = (RationalBSplineCurve2) segment;
            localParam = spline.startParameter() + localParam * (spline.endParameter() - spline.startParameter());
        }
        return segment.pointAt(localParam);
    }

    @Override
    public boolean contains(Point2 point) {
        Preconditions.requireNonNull(point, "point");
        for (Curve2 segment : segments) {
            if (segment.contains(point)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public List<Point2> sample(int segments) {
        List<Point2> points = new ArrayList<>();
        for (Curve2 seg : this.segments) {
            points.addAll(seg.sample(segments / this.segments.size() + 1));
        }
        return List.copyOf(points);
    }

    @Override
    public double length() {
        double total = 0.0;
        for (Curve2 segment : segments) total += segment.length();
        return total;
    }
}
