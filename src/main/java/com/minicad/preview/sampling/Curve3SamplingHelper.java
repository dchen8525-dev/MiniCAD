package com.minicad.preview.sampling;

import com.minicad.common.Epsilon;
import com.minicad.common.TrimmedWindowWalk;
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

    /** The 3D metric the shared walk needs; the walk itself is dimension-free. */
    private static final TrimmedWindowWalk.PointDistance<CartesianPoint> POINT_DISTANCE =
            CartesianPoint::distanceTo;

    private Curve3SamplingHelper() {
        // Utility class
    }

    @FunctionalInterface
    private interface LooseCurveSampler {
        List<CartesianPoint> sample(Curve3 curve);
    }

    private record LooseCurveRule(Class<? extends Curve3> type, LooseCurveSampler sampler) {
        boolean matches(Curve3 curve) {
            return type.isInstance(curve);
        }
    }

    /**
     * Ordered loose-curve dispatch table, first match wins; the type order is
     * frozen by {@code Curve3LooseSampleDispatchTableTest}. All 4 types are
     * final classes implementing Curve3 directly (no subtype relation today),
     * so the order is currently behaviour neutral. Curves matching no rule
     * (Line3, Circle, Ellipse3, B-splines) fall through to the shared
     * {@code curve.sample(72)} + empty-check tail.
     */
    private static final List<LooseCurveRule> LOOSE_CURVE_SAMPLERS = List.of(
            new LooseCurveRule(TrimmedCurve3.class, curve -> sampleTrimmedCurve3((TrimmedCurve3) curve, 72)),
            new LooseCurveRule(SurfaceCurve3.class, curve -> sampleLooseCurve(((SurfaceCurve3) curve).curve3d())),
            new LooseCurveRule(Polyline3.class, curve -> ((Polyline3) curve).points()),
            new LooseCurveRule(CompositeCurve3.class, curve -> compositePoints((CompositeCurve3) curve)));

    /**
     * Samples any loose 3D curve without requiring edge context. The single
     * shared implementation — the export/preview copies delegate here.
     */
    public static List<CartesianPoint> sampleLooseCurve(Curve3 curve) {
        for (LooseCurveRule rule : LOOSE_CURVE_SAMPLERS) {
            if (rule.matches(curve)) {
                return rule.sampler().sample(curve);
            }
        }
        List<CartesianPoint> points = curve.sample(72);
        if (points.isEmpty()) {
            throw new UnsupportedGeometryException("curve sampling for " + curve.getClass().getSimpleName() + " is unsupported");
        }
        return points;
    }

    private static List<CartesianPoint> compositePoints(CompositeCurve3 compositeCurve) {
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
        int startIndex = TrimmedWindowWalk.nearestIndex(basisPoints, trimmedCurve.trimStart(), POINT_DISTANCE);
        int endIndex = TrimmedWindowWalk.nearestIndex(basisPoints, trimmedCurve.trimEnd(), POINT_DISTANCE);

        List<CartesianPoint> trimmed = new ArrayList<>(Math.max(segments + 1, 2));
        trimmed.add(trimmedCurve.trimStart());
        if (closed) {
            TrimmedWindowWalk.appendClosed(
                    trimmed, basisPoints, startIndex, endIndex, trimmedCurve.senseAgreement(), POINT_DISTANCE);
        } else {
            TrimmedWindowWalk.appendOpen(trimmed, basisPoints, startIndex, endIndex, POINT_DISTANCE);
        }
        TrimmedWindowWalk.addDistinct(trimmed, trimmedCurve.trimEnd(), POINT_DISTANCE);
        return List.copyOf(trimmed);
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
