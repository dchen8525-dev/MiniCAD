package com.minicad.preview.sampling;

import com.minicad.common.Epsilon;
import com.minicad.common.UnsupportedGeometryException;
import com.minicad.geometry.CartesianPoint;
import com.minicad.geometry.Circle;
import com.minicad.geometry.CompositeCurve3;
import com.minicad.geometry.Curve3;
import com.minicad.geometry.Ellipse3;
import com.minicad.geometry.Polyline3;
import com.minicad.geometry.SurfaceCurve3;
import com.minicad.geometry.TrimmedCurve3;

import java.util.ArrayList;
import java.util.List;

/**
 * Helper class for 3D curve sampling utilities.
 * Extracted from StepPreviewJsonExporter for better maintainability.
 */
public final class Curve3SamplingHelper {

    private Curve3SamplingHelper() {
        // Utility class
    }

    /**
     * Samples any loose 3D curve without requiring edge context. The single
     * shared implementation — the export/preview copies delegate here.
     */
    public static List<CartesianPoint> sampleLooseCurve(Curve3 curve) {
        if (curve instanceof TrimmedCurve3) {
            TrimmedCurve3 trimmedCurve = (TrimmedCurve3) curve;
            return sampleTrimmedCurve3(trimmedCurve, 72);
        }
        if (curve instanceof SurfaceCurve3) {
            SurfaceCurve3 surfaceCurve = (SurfaceCurve3) curve;
            return sampleLooseCurve(surfaceCurve.curve3d());
        }
        if (curve instanceof Polyline3) {
            Polyline3 polyline = (Polyline3) curve;
            return polyline.points();
        }
        if (curve instanceof CompositeCurve3) {
            CompositeCurve3 compositeCurve = (CompositeCurve3) curve;
            List<CartesianPoint> points = new ArrayList<>();
            boolean first = true;
            for (Curve3 segment : compositeCurve.segments()) {
                List<CartesianPoint> segmentPoints = sampleLooseCurve(segment);
                int start = first ? 0 : 1;
                for (int i = start; i < segmentPoints.size(); i++) {
                    points.add(segmentPoints.get(i));
                }
                first = false;
            }
            return List.copyOf(points);
        }
        List<CartesianPoint> points = curve.sample(72);
        if (points.isEmpty()) {
            throw new UnsupportedGeometryException("curve sampling for " + curve.getClass().getSimpleName() + " is unsupported");
        }
        return points;
    }

    public static double arcSweep(double startAngle, double endAngle, boolean closed, boolean naturalForward) {
        double delta = endAngle - startAngle;
        if (closed) {
            return naturalForward ? Math.PI * 2.0 : -Math.PI * 2.0;
        }
        if (naturalForward) {
            return delta < 0.0 ? delta + Math.PI * 2.0 : delta;
        }
        return delta > 0.0 ? delta - Math.PI * 2.0 : delta;
    }

    public static List<CartesianPoint> sampleTrimmedCurve3(TrimmedCurve3 trimmedCurve, int segments) {
        List<CartesianPoint> sampled = sampleLooseCurve(trimmedCurve.basisCurve());
        if (sampled.size() < 2) {
            return List.of(trimmedCurve.trimStart(), trimmedCurve.trimEnd());
        }
        boolean closed = sampled.get(0).distanceTo(sampled.get(sampled.size() - 1)) <= 1.0e-9;
        List<CartesianPoint> basisPoints = closed ? List.copyOf(sampled.subList(0, sampled.size() - 1)) : sampled;
        int startIndex = nearestPointIndex(basisPoints, trimmedCurve.trimStart());
        int endIndex = nearestPointIndex(basisPoints, trimmedCurve.trimEnd());

        List<CartesianPoint> trimmed = new ArrayList<>(Math.max(segments + 1, 2));
        trimmed.add(trimmedCurve.trimStart());
        if (closed) {
            appendClosedTrimmedPoints(trimmed, basisPoints, startIndex, endIndex, trimmedCurve.senseAgreement());
        } else {
            appendOpenTrimmedPoints(trimmed, basisPoints, startIndex, endIndex);
        }
        addDistinctPoint(trimmed, trimmedCurve.trimEnd());
        return List.copyOf(trimmed);
    }

    public static int nearestPointIndex(List<CartesianPoint> points, CartesianPoint target) {
        int nearestIndex = 0;
        double nearestDistance = Double.POSITIVE_INFINITY;
        for (int index = 0; index < points.size(); index++) {
            double distance = points.get(index).distanceTo(target);
            if (distance < nearestDistance) {
                nearestDistance = distance;
                nearestIndex = index;
            }
        }
        return nearestIndex;
    }

    public static void appendClosedTrimmedPoints(
            List<CartesianPoint> target,
            List<CartesianPoint> basisPoints,
            int startIndex,
            int endIndex,
            boolean senseAgreement
    ) {
        int size = basisPoints.size();
        int index = startIndex;
        while (index != endIndex) {
            index = senseAgreement ? (index + 1) % size : (index - 1 + size) % size;
            addDistinctPoint(target, basisPoints.get(index));
        }
    }

    public static void appendOpenTrimmedPoints(
            List<CartesianPoint> target,
            List<CartesianPoint> basisPoints,
            int startIndex,
            int endIndex
    ) {
        if (startIndex <= endIndex) {
            for (int index = startIndex + 1; index <= endIndex; index++) {
                addDistinctPoint(target, basisPoints.get(index));
            }
            return;
        }
        for (int index = startIndex - 1; index >= endIndex; index--) {
            addDistinctPoint(target, basisPoints.get(index));
        }
    }

    public static void addDistinctPoint(List<CartesianPoint> points, CartesianPoint candidate) {
        if (points.isEmpty() || points.get(points.size() - 1).distanceTo(candidate) > 1.0e-9) {
            points.add(candidate);
        }
    }

    public static List<CartesianPoint> sampleCircleArc(Circle circle, CartesianPoint start, CartesianPoint end, boolean naturalForward) {
        // Project points onto circle if they're close (numerical tolerance)
        CartesianPoint projectedStart = circle.contains(start) ? start : circle.closestPointTo(start);
        CartesianPoint projectedEnd = circle.contains(end) ? end : circle.closestPointTo(end);
        double startAngle = circle.angleOf(projectedStart);
        double endAngle = circle.angleOf(projectedEnd);
        double delta = endAngle - startAngle;
        if (projectedStart.distanceTo(projectedEnd) <= Epsilon.EPS) {
            delta = naturalForward ? Math.PI * 2.0 : -Math.PI * 2.0;
        } else if (naturalForward) {
            if (delta < 0.0) {
                delta += Math.PI * 2.0;
            }
        } else if (delta > 0.0) {
            delta -= Math.PI * 2.0;
        }

        int segments = Math.max(64, (int) Math.ceil(Math.abs(delta) / (Math.PI / 72.0)));
        List<CartesianPoint> points = new ArrayList<>(segments + 1);
        for (int i = 0; i <= segments; i++) {
            double angle = startAngle + delta * i / segments;
            points.add(circle.pointAt(angle));
        }
        points.set(0, start);
        points.set(points.size() - 1, end);
        return points;
    }

    public static List<CartesianPoint> sampleEllipseArc(Ellipse3 ellipse, CartesianPoint start, CartesianPoint end, boolean naturalForward) {
        double startAngle = ellipse.angleOf(start);
        double endAngle = ellipse.angleOf(end);
        double delta = endAngle - startAngle;
        if (start.distanceTo(end) <= Epsilon.EPS) {
            delta = naturalForward ? Math.PI * 2.0 : -Math.PI * 2.0;
        } else if (naturalForward) {
            if (delta < 0.0) {
                delta += Math.PI * 2.0;
            }
        } else if (delta > 0.0) {
            delta -= Math.PI * 2.0;
        }

        int segments = Math.max(72, (int) Math.ceil(Math.abs(delta) / (Math.PI / 96.0)));
        List<CartesianPoint> points = new ArrayList<>(segments + 1);
        for (int i = 0; i <= segments; i++) {
            double angle = startAngle + delta * i / segments;
            points.add(ellipse.pointAt(angle));
        }
        points.set(0, start);
        points.set(points.size() - 1, end);
        return points;
    }
}
