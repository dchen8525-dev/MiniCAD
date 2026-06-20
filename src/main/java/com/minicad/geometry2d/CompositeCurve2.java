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
        return segments == null ? 0 : segments.size();
    }

    /**
     * Returns the parameter value corresponding to a point on this curve.
     *
     * @param point the point to find parameter for
     * @return parameter value (approximate)
     */
    public double parameterOf(Point2 point) {
        Preconditions.requireNonNull(point, "point");
        if (segments == null || segments.isEmpty()) {
            return 0.0;
        }
        // Find which segment contains the point and return parameter
        double accumulatedLength = 0.0;
        for (int i = 0; i < segments.size(); i++) {
            Curve2 seg = segments.get(i);
            if (seg.contains(point)) {
                double local = 0.0;
                if (seg instanceof TrimmedCurve2) {
                    TrimmedCurve2 trimmed = (TrimmedCurve2) seg;
                    double underlying = trimmed.parameterOnUnderlyingCurve(point);
                    double span = trimmed.trimParamEnd() - trimmed.trimParamStart();
                    local = (underlying - trimmed.trimParamStart()) / span;
                    if (!trimmed.senseAgreement()) local = 1.0 - local;
                }
                return (i + local) / segments.size();
            }
        }
        return 1.0;
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
        if (segments == null || segments.isEmpty()) {
            return new Point2(0, 0);
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
    public boolean contains(Point2 point) {
        Preconditions.requireNonNull(point, "point");
        if (segments == null || segments.isEmpty()) {
            return false;
        }
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
        if (this.segments == null || this.segments.isEmpty()) {
            return List.copyOf(points);
        }
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
