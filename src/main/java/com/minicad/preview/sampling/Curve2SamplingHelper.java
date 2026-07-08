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
final class Curve2SamplingHelper {

    private Curve2SamplingHelper() {
        // Utility class
    }

    static List<Point2> sampleLooseCurve2(Curve2 curve) {
        if (curve instanceof Line2) {
            Line2 line = (Line2) curve;
            return List.of(line.pointAt(0.0), line.pointAt(1.0));
        }
        if (curve instanceof Circle2) {
            Circle2 circle = (Circle2) curve;
            return sampleCircle2Points(circle, 72);
        }
        if (curve instanceof Ellipse2) {
            Ellipse2 ellipse = (Ellipse2) curve;
            return sampleEllipse2Points(ellipse, 72);
        }
        if (curve instanceof Parabola2) {
            Parabola2 parabola = (Parabola2) curve;
            return parabola.sample(72);
        }
        if (curve instanceof Hyperbola2) {
            Hyperbola2 hyperbola = (Hyperbola2) curve;
            return hyperbola.sample(72);
        }
        if (curve instanceof DegenerateCurve2) {
            DegenerateCurve2 degenerate = (DegenerateCurve2) curve;
            return List.of(degenerate.point());
        }
        if (curve instanceof BSplineCurve2) {
            BSplineCurve2 spline = (BSplineCurve2) curve;
            return spline.sample(72);
        }
        if (curve instanceof RationalBSplineCurve2) {
            RationalBSplineCurve2 spline = (RationalBSplineCurve2) curve;
            return spline.sample(72);
        }
        if (curve instanceof TrimmedCurve2) {
            TrimmedCurve2 trimmedCurve = (TrimmedCurve2) curve;
            return sampleTrimmedCurve2(trimmedCurve, 72);
        }
        if (curve instanceof Polyline2) {
            Polyline2 polyline = (Polyline2) curve;
            return polyline.points();
        }
        if (curve instanceof CompositeCurve2) {
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
        }
        throw new UnsupportedGeometryException("2D curve sampling for " + curveTypeName(curve) + " is unsupported");
    }

    static List<Point2> sampleTrimmedCurve2(TrimmedCurve2 trimmedCurve, int segments) {
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

    static int nearestPointIndex2(List<Point2> points, Point2 target) {
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

    static void appendClosedTrimmedPoints2(
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

    static void appendOpenTrimmedPoints2(
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

    static void addDistinctPoint2(List<Point2> points, Point2 candidate) {
        if (points.isEmpty() || points.get(points.size() - 1).subtract(candidate).norm() > 1.0e-9) {
            points.add(candidate);
        }
    }

    static String curveTypeName(Curve3 curve) {
        if (curve instanceof Line3) {
            return "LINE";
        }
        if (curve instanceof Circle) {
            return "CIRCLE";
        }
        if (curve instanceof Ellipse3) {
            return "ELLIPSE";
        }
        if (curve instanceof Parabola3) {
            return "PARABOLA";
        }
        if (curve instanceof Hyperbola3) {
            return "HYPERBOLA";
        }
        if (curve instanceof Clothoid3) {
            return "CLOTHOID";
        }
        if (curve instanceof DegenerateCurve3) {
            return "DEGENERATE_CURVE";
        }
        if (curve instanceof BSplineCurve3) {
            return "B_SPLINE_CURVE";
        }
        if (curve instanceof RationalBSplineCurve3) {
            return "RATIONAL_B_SPLINE_CURVE";
        }
        if (curve instanceof TrimmedCurve3) {
            return "TRIMMED_CURVE";
        }
        if (curve instanceof SurfaceCurve3) {
            return "SURFACE_CURVE";
        }
        if (curve instanceof Polyline3) {
            return "POLYLINE";
        }
        if (curve instanceof CompositeCurve3) {
            return "COMPOSITE_CURVE";
        }
        return curve.getClass().getSimpleName();
    }

    static String curveTypeName(Curve2 curve) {
        if (curve instanceof Line2) {
            return "LINE";
        }
        if (curve instanceof Circle2) {
            return "CIRCLE";
        }
        if (curve instanceof Ellipse2) {
            return "ELLIPSE";
        }
        if (curve instanceof Parabola2) {
            return "PARABOLA";
        }
        if (curve instanceof Hyperbola2) {
            return "HYPERBOLA";
        }
        if (curve instanceof DegenerateCurve2) {
            return "DEGENERATE_CURVE";
        }
        if (curve instanceof BSplineCurve2) {
            return "B_SPLINE_CURVE";
        }
        if (curve instanceof RationalBSplineCurve2) {
            return "RATIONAL_B_SPLINE_CURVE";
        }
        if (curve instanceof TrimmedCurve2) {
            return "TRIMMED_CURVE";
        }
        if (curve instanceof Polyline2) {
            return "POLYLINE";
        }
        if (curve instanceof CompositeCurve2) {
            return "COMPOSITE_CURVE";
        }
        return curve.getClass().getSimpleName();
    }

    static List<Point2> sampleCircle2Points(Circle2 circle, int segments) {
        List<Point2> points = new ArrayList<>(segments + 1);
        for (int index = 0; index <= segments; index++) {
            points.add(circle.pointAt(Math.PI * 2.0 * index / segments));
        }
        return List.copyOf(points);
    }

    static List<Point2> sampleEllipse2Points(Ellipse2 ellipse, int segments) {
        List<Point2> points = new ArrayList<>(segments + 1);
        for (int index = 0; index <= segments; index++) {
            points.add(ellipse.pointAt(Math.PI * 2.0 * index / segments));
        }
        return List.copyOf(points);
    }
}