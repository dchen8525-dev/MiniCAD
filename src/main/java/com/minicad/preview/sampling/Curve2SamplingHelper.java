package com.minicad.preview.sampling;

import com.minicad.common.UnsupportedGeometryException;
import com.minicad.geometry.Circle;
import com.minicad.geometry.Ellipse3;
import com.minicad.geometry.Hyperbola3;
import com.minicad.geometry.Parabola3;
import com.minicad.geometry.Clothoid3;
import com.minicad.geometry.DegenerateCurve3;
import com.minicad.geometry.Line3;
import com.minicad.geometry.Polyline3;
import com.minicad.geometry.CompositeCurve3;
import com.minicad.step.semantic.StepEntityNamingUtils;
import com.minicad.geometry.BSplineCurve3;
import com.minicad.geometry.RationalBSplineCurve3;
import com.minicad.geometry.TrimmedCurve3;
import com.minicad.geometry.SurfaceCurve3;
import com.minicad.geometry2d.Circle2;
import com.minicad.geometry2d.Ellipse2;
import com.minicad.geometry2d.Hyperbola2;
import com.minicad.geometry2d.Parabola2;
import com.minicad.geometry2d.DegenerateCurve2;
import com.minicad.geometry2d.Line2;
import com.minicad.geometry2d.Polyline2;
import com.minicad.geometry2d.CompositeCurve2;
import com.minicad.geometry2d.BSplineCurve2;
import com.minicad.geometry2d.RationalBSplineCurve2;
import com.minicad.geometry2d.TrimmedCurve2;
import com.minicad.geometry2d.Point2;
import com.minicad.geometry.Curve3;
import com.minicad.geometry2d.Curve2;

import java.util.ArrayList;
import java.util.List;

/**
 * Helper class for 2D curve sampling and type name utilities.
 * Extracted from StepPreviewJsonExporter for better maintainability.
 */
public final class Curve2SamplingHelper {

    private Curve2SamplingHelper() {
        // Utility class
    }

    @FunctionalInterface
    private interface Curve2SampleHandler {
        List<Point2> sample(Curve2 curve);
    }

    private record Curve2SampleRule(Class<?> type, Curve2SampleHandler handler) {
        boolean matches(Curve2 curve) {
            return type.isInstance(curve);
        }
    }

    private static Curve2SampleRule curve2SampleRule(Class<?> type, Curve2SampleHandler handler) {
        return new Curve2SampleRule(type, handler);
    }

    /** Types whose loose sample is the interface's own sample(72). */
    private static Curve2SampleRule sampledRule(Class<?> type) {
        return curve2SampleRule(type, (curve) -> curve.sample(72));
    }

    /**
     * Loose 2D curve sampling rules keyed by concrete curve type, replacing the
     * former 11-branch if/else-if chain. Order mirrors the original chain
     * (first match wins); a curve matching no rule throws
     * UnsupportedGeometryException, as the old trailing statement did. The
     * sampledRule bodies call Curve2.sample directly: the original branches
     * downcast to the concrete type first, but every listed type overrides the
     * interface default, so dynamic dispatch reaches the same method.
     */
    private static final List<Curve2SampleRule> CURVE2_SAMPLE_RULES = List.of(
            curve2SampleRule(Line2.class, (curve) -> {
                Line2 line = (Line2) curve;
                return List.of(line.pointAt(0.0), line.pointAt(1.0));
            }),
            curve2SampleRule(Circle2.class, (curve) -> sampleCircle2Points((Circle2) curve, 72)),
            curve2SampleRule(Ellipse2.class, (curve) -> sampleEllipse2Points((Ellipse2) curve, 72)),
            sampledRule(Parabola2.class),
            sampledRule(Hyperbola2.class),
            curve2SampleRule(DegenerateCurve2.class, (curve) -> {
                DegenerateCurve2 degenerate = (DegenerateCurve2) curve;
                return List.of(degenerate.point());
            }),
            sampledRule(BSplineCurve2.class),
            sampledRule(RationalBSplineCurve2.class),
            curve2SampleRule(TrimmedCurve2.class, (curve) -> {
                TrimmedCurve2 trimmedCurve = (TrimmedCurve2) curve;
                return sampleTrimmedCurve2(trimmedCurve, 72);
            }),
            curve2SampleRule(Polyline2.class, (curve) -> {
                Polyline2 polyline = (Polyline2) curve;
                return polyline.points();
            }),
            curve2SampleRule(CompositeCurve2.class, (curve) -> {
                CompositeCurve2 compositeCurve = (CompositeCurve2) curve;
                List<Point2> points = new ArrayList<>();
                boolean first = true;
                for (Curve2 segment : compositeCurve.segments()) {
                    List<Point2> segmentPoints = sampleLooseCurve2(segment);
                    int start = first ? 0 : 1;
                    for (int i = start; i < segmentPoints.size(); i++) {
                        points.add(segmentPoints.get(i));
                    }
                    first = false;
                }
                return List.copyOf(points);
            })
    );

    public static List<Point2> sampleLooseCurve2(Curve2 curve) {
        for (Curve2SampleRule rule : CURVE2_SAMPLE_RULES) {
            if (rule.matches(curve)) {
                return rule.handler().sample(curve);
            }
        }
        throw new UnsupportedGeometryException("2D curve sampling for " + curveTypeName(curve) + " is unsupported");
    }

    public static List<Point2> sampleTrimmedCurve2(TrimmedCurve2 trimmedCurve, int segments) {
        List<Point2> sampled = sampleLooseCurve2(trimmedCurve.basisCurve());
        if (sampled.size() < 2) {
            return List.of(trimmedCurve.trimStart(), trimmedCurve.trimEnd());
        }
        boolean closed = sampled.get(0).subtract(sampled.get(sampled.size() - 1)).norm() <= 1.0e-9;
        List<Point2> basisPoints = closed ? List.copyOf(sampled.subList(0, sampled.size() - 1)) : sampled;
        int startIndex = nearestPointIndex2(basisPoints, trimmedCurve.trimStart());
        int endIndex = nearestPointIndex2(basisPoints, trimmedCurve.trimEnd());

        List<Point2> trimmed = new ArrayList<>(Math.max(segments + 1, 2));
        trimmed.add(trimmedCurve.trimStart());
        if (closed) {
            appendClosedTrimmedPoints2(trimmed, basisPoints, startIndex, endIndex, trimmedCurve.senseAgreement());
        } else {
            appendOpenTrimmedPoints2(trimmed, basisPoints, startIndex, endIndex);
        }
        addDistinctPoint2(trimmed, trimmedCurve.trimEnd());
        return List.copyOf(trimmed);
    }

    public static int nearestPointIndex2(List<Point2> points, Point2 target) {
        int nearestIndex = 0;
        double nearestDistance = Double.POSITIVE_INFINITY;
        for (int index = 0; index < points.size(); index++) {
            double distance = points.get(index).subtract(target).norm();
            if (distance < nearestDistance) {
                nearestDistance = distance;
                nearestIndex = index;
            }
        }
        return nearestIndex;
    }

    public static void appendClosedTrimmedPoints2(
            List<Point2> target,
            List<Point2> basisPoints,
            int startIndex,
            int endIndex,
            boolean senseAgreement
    ) {
        int size = basisPoints.size();
        int index = startIndex;
        while (index != endIndex) {
            index = senseAgreement ? (index + 1) % size : (index - 1 + size) % size;
            addDistinctPoint2(target, basisPoints.get(index));
        }
    }

    public static void appendOpenTrimmedPoints2(
            List<Point2> target,
            List<Point2> basisPoints,
            int startIndex,
            int endIndex
    ) {
        if (startIndex <= endIndex) {
            for (int index = startIndex + 1; index <= endIndex; index++) {
                addDistinctPoint2(target, basisPoints.get(index));
            }
            return;
        }
        for (int index = startIndex - 1; index >= endIndex; index--) {
            addDistinctPoint2(target, basisPoints.get(index));
        }
    }

    public static void addDistinctPoint2(List<Point2> points, Point2 candidate) {
        if (points.isEmpty() || points.get(points.size() - 1).subtract(candidate).norm() > 1.0e-9) {
            points.add(candidate);
        }
    }

    public static String curveTypeName(Curve3 curve) {
        return StepEntityNamingUtils.curveTypeName(curve);
    }

    public static String curveTypeName(Curve2 curve) {
        return StepEntityNamingUtils.curveTypeName(curve);
    }

    public static List<Point2> sampleCircle2Points(Circle2 circle, int segments) {
        List<Point2> points = new ArrayList<>(segments + 1);
        for (int index = 0; index <= segments; index++) {
            points.add(circle.pointAt(Math.PI * 2.0 * index / segments));
        }
        return List.copyOf(points);
    }

    public static List<Point2> sampleEllipse2Points(Ellipse2 ellipse, int segments) {
        List<Point2> points = new ArrayList<>(segments + 1);
        for (int index = 0; index <= segments; index++) {
            points.add(ellipse.pointAt(Math.PI * 2.0 * index / segments));
        }
        return List.copyOf(points);
    }
}
