package com.minicad.preview.sampling;

import com.minicad.common.Epsilon;
import com.minicad.geometry2d.BSplineCurve2;
import com.minicad.geometry2d.Circle2;
import com.minicad.geometry2d.Curve2;
import com.minicad.geometry2d.Ellipse2;
import com.minicad.geometry2d.Line2;
import com.minicad.geometry2d.Point2;
import com.minicad.geometry2d.TrimmedCurve2;
import com.minicad.geometry2d.Vector2;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Pcurve sampling utilities: snap and sample methods for 2D curves.
 * Extracted from PreviewUvMapper for code organization.
 */
public final class PreviewPcurveSampler {

    private PreviewPcurveSampler() {}

    // ─── Pcurve snap helpers ────────────────────────────────────────────────

    public static UvPoint snapToLine(UvPoint point, Line2 line) {
        Point2 snapped = line.closestPoint(new Point2(point.u(), point.v()));
        return new UvPoint(snapped.x(), snapped.y());
    }

    public static UvPoint snapToCircle(UvPoint point, Circle2 circle) {
        Vector2 offset = new Point2(point.u(), point.v()).subtract(circle.center());
        double norm = offset.norm();
        if (norm <= Epsilon.EPS) {
            Point2 fallback = circle.pointAt(0.0);
            return new UvPoint(fallback.x(), fallback.y());
        }
        Point2 snapped = circle.center().add(offset.scale(circle.radius() / norm));
        return new UvPoint(snapped.x(), snapped.y());
    }

    public static UvPoint snapToEllipse(UvPoint point, Ellipse2 ellipse) {
        double angle = ellipse.angleOf(ellipse.pointAt(ellipse.angleOf(snapEllipseSeed(point, ellipse))));
        Point2 snapped = ellipse.pointAt(angle);
        return new UvPoint(snapped.x(), snapped.y());
    }

    public static Point2 snapEllipseSeed(UvPoint point, Ellipse2 ellipse) {
        Vector2 offset = new Point2(point.u(), point.v()).subtract(ellipse.center());
        if (offset.norm() <= Epsilon.EPS) {
            return ellipse.pointAt(0.0);
        }
        Vector2 x = ellipse.xDirection().asVector();
        Vector2 y = new Vector2(-x.y(), x.x());
        double nx = offset.dot(x) / ellipse.semiAxis1();
        double ny = offset.dot(y) / ellipse.semiAxis2();
        double norm = Math.hypot(nx, ny);
        if (norm <= Epsilon.EPS) {
            return ellipse.pointAt(0.0);
        }
        double angle = Math.atan2(ny / norm, nx / norm);
        return ellipse.pointAt(angle);
    }

    // ─── Pcurve sample helpers ───────────────────────────────────────────────

    public static List<UvPoint> sampleLinePcurve(Line2 line, UvPoint start, UvPoint end) {
        Point2 startPoint = new Point2(start.u(), start.v());
        Point2 endPoint = new Point2(end.u(), end.v());
        double startParameter = line.parameterOf(startPoint);
        double endParameter = line.parameterOf(endPoint);
        int segments = Math.max(12, (int) Math.ceil(Math.abs(endParameter - startParameter) * 6.0));
        List<UvPoint> points = new ArrayList<>(segments + 1);
        for (int index = 0; index <= segments; index++) {
            double parameter = startParameter + (endParameter - startParameter) * index / segments;
            Point2 point = line.pointAt(parameter);
            points.add(new UvPoint(point.x(), point.y()));
        }
        points.set(0, start);
        points.set(points.size() - 1, end);
        return List.copyOf(points);
    }

    public static List<UvPoint> sampleSplinePcurve(BSplineCurve2 spline, UvPoint projectedStart, UvPoint projectedEnd) {
        List<Point2> sampled = spline.sample(48);
        if (sampled.size() < 2) {
            return List.of();
        }
        int startIndex = closestPointIndex(sampled, projectedStart);
        int endIndex = closestPointIndex(sampled, projectedEnd);
        if (startIndex == endIndex) {
            return List.of(projectedStart, projectedEnd);
        }
        List<UvPoint> points = new ArrayList<>();
        int step = startIndex <= endIndex ? 1 : -1;
        for (int index = startIndex; index != endIndex + step; index += step) {
            Point2 point = sampled.get(index);
            points.add(new UvPoint(point.x(), point.y()));
        }
        points.set(0, projectedStart);
        points.set(points.size() - 1, projectedEnd);
        return List.copyOf(points);
    }

    public static List<UvPoint> sampleCirclePcurve(Circle2 circle, UvPoint start, UvPoint end) {
        Point2 startPoint = new Point2(start.u(), start.v());
        Point2 endPoint = new Point2(end.u(), end.v());
        double startAngle = circle.angleOf(startPoint);
        double endAngle = circle.angleOf(endPoint);
        double delta = endAngle - startAngle;
        if (delta > Math.PI) {
            delta -= Math.PI * 2.0;
        } else if (delta < -Math.PI) {
            delta += Math.PI * 2.0;
        }
        int segments = Math.max(18, (int) Math.ceil(Math.abs(delta) * 18.0));
        List<UvPoint> points = new ArrayList<>(segments + 1);
        for (int index = 0; index <= segments; index++) {
            double angle = startAngle + delta * index / segments;
            Point2 point = circle.pointAt(angle);
            points.add(new UvPoint(point.x(), point.y()));
        }
        points.set(0, start);
        points.set(points.size() - 1, end);
        return List.copyOf(points);
    }

    public static List<UvPoint> sampleEllipsePcurve(Ellipse2 ellipse, UvPoint start, UvPoint end) {
        Point2 startPoint = new Point2(start.u(), start.v());
        Point2 endPoint = new Point2(end.u(), end.v());
        double startAngle = ellipse.angleOf(startPoint);
        double endAngle = ellipse.angleOf(endPoint);
        double delta = endAngle - startAngle;
        if (delta > Math.PI) {
            delta -= Math.PI * 2.0;
        } else if (delta < -Math.PI) {
            delta += Math.PI * 2.0;
        }
        int segments = Math.max(18, (int) Math.ceil(Math.abs(delta) * 18.0));
        List<UvPoint> points = new ArrayList<>(segments + 1);
        for (int index = 0; index <= segments; index++) {
            double angle = startAngle + delta * index / segments;
            Point2 point = ellipse.pointAt(angle);
            points.add(new UvPoint(point.x(), point.y()));
        }
        points.set(0, start);
        points.set(points.size() - 1, end);
        return List.copyOf(points);
    }

    public static List<UvPoint> sampleTrimmedPcurve(TrimmedCurve2 trimmed, UvPoint projectedStart, UvPoint projectedEnd) {
        UvPoint trimStart = new UvPoint(trimmed.trimStart().x(), trimmed.trimStart().y());
        UvPoint trimEnd = new UvPoint(trimmed.trimEnd().x(), trimmed.trimEnd().y());
        List<UvPoint> forward = sampleCurve2(trimmed.basisCurve(), trimStart, trimEnd);
        List<UvPoint> reverse = sampleCurve2(trimmed.basisCurve(), trimEnd, trimStart);
        if (forward.isEmpty() && reverse.isEmpty()) {
            return List.of();
        }
        List<UvPoint> preferred;
        if (!trimmed.senseAgreement()) {
            preferred = reverse.isEmpty() ? forward : reverse;
        } else {
            preferred = score(projectedStart, projectedEnd, forward) <= score(projectedStart, projectedEnd, reverse)
                    ? forward
                    : reverse;
        }
        return alignTrimmedSamples(preferred, projectedStart, projectedEnd);
    }

    public static List<UvPoint> sampleCurve2(Curve2 curve, UvPoint start, UvPoint end) {
        if (curve instanceof Line2) {
            return sampleLinePcurve((Line2) curve, start, end);
        }
        if (curve instanceof Circle2) {
            return sampleCirclePcurve((Circle2) curve, start, end);
        }
        if (curve instanceof Ellipse2) {
            return sampleEllipsePcurve((Ellipse2) curve, start, end);
        }
        if (curve instanceof BSplineCurve2) {
            return sampleSplinePcurve((BSplineCurve2) curve, start, end);
        }
        if (curve instanceof TrimmedCurve2) {
            return sampleTrimmedPcurve((TrimmedCurve2) curve, start, end);
        }
        return List.of();
    }

    // ─── Utility helpers ────────────────────────────────────────────────────

    public static double score(UvPoint start, UvPoint end, List<UvPoint> samples) {
        if (samples.isEmpty()) {
            return Double.POSITIVE_INFINITY;
        }
        return distanceSquared(start, samples.get(0)) + distanceSquared(end, samples.get(samples.size() - 1));
    }

    public static List<UvPoint> alignTrimmedSamples(List<UvPoint> samples, UvPoint projectedStart, UvPoint projectedEnd) {
        if (samples.isEmpty()) {
            return samples;
        }
        List<UvPoint> aligned = new ArrayList<>(samples);
        double forwardScore = distanceSquared(projectedStart, aligned.get(0)) + distanceSquared(projectedEnd, aligned.get(aligned.size() - 1));
        double reverseScore = distanceSquared(projectedStart, aligned.get(aligned.size() - 1)) + distanceSquared(projectedEnd, aligned.get(0));
        if (reverseScore < forwardScore) {
            Collections.reverse(aligned);
        }
        aligned.set(0, projectedStart);
        aligned.set(aligned.size() - 1, projectedEnd);
        return List.copyOf(aligned);
    }

    public static int closestPointIndex(List<Point2> points, UvPoint target) {
        int bestIndex = 0;
        double bestDistance = Double.POSITIVE_INFINITY;
        for (int index = 0; index < points.size(); index++) {
            Point2 point = points.get(index);
            double du = point.x() - target.u();
            double dv = point.y() - target.v();
            double distance = du * du + dv * dv;
            if (distance < bestDistance) {
                bestDistance = distance;
                bestIndex = index;
            }
        }
        return bestIndex;
    }

    public static boolean sameUv(UvPoint left, UvPoint right) {
        return distanceSquared(left, right) <= 1.0e-12;
    }

    public static double distanceSquared(UvPoint left, UvPoint right) {
        double du = left.u() - right.u();
        double dv = left.v() - right.v();
        return du * du + dv * dv;
    }

    // ─── Polygon containment helpers ────────────────────────────────────────

    public static double signedArea(List<UvPoint> points) {
        if (points.size() < 3) {
            return 0.0;
        }
        double area = 0.0;
        for (int index = 0; index + 1 < points.size(); index++) {
            UvPoint current = points.get(index);
            UvPoint next = points.get(index + 1);
            area += current.u() * next.v() - next.u() * current.v();
        }
        return area * 0.5;
    }

    public static boolean contains(List<UvPoint> polygon, UvPoint point) {
        if (polygon.size() < 3) {
            return false;
        }
        if (isOnPolygonBoundary(polygon, point)) {
            return true;
        }
        boolean inside = false;
        for (int i = 0, j = polygon.size() - 1; i < polygon.size(); j = i++) {
            UvPoint a = polygon.get(i);
            UvPoint b = polygon.get(j);
            boolean intersects = ((a.v() > point.v()) != (b.v() > point.v()))
                    && (point.u() < (b.u() - a.u()) * (point.v() - a.v()) / ((b.v() - a.v()) + 1.0e-12) + a.u());
            if (intersects) {
                inside = !inside;
            }
        }
        return inside;
    }

    public static boolean isOnPolygonBoundary(List<UvPoint> polygon, UvPoint point) {
        for (int index = 0; index + 1 < polygon.size(); index++) {
            if (isOnSegment(polygon.get(index), polygon.get(index + 1), point)) {
                return true;
            }
        }
        return false;
    }

    public static boolean isOnSegment(UvPoint a, UvPoint b, UvPoint point) {
        double abU = b.u() - a.u();
        double abV = b.v() - a.v();
        double lengthSquared = abU * abU + abV * abV;
        if (lengthSquared <= 1.0e-18) {
            return distanceSquared(a, point) <= 1.0e-18;
        }
        double apU = point.u() - a.u();
        double apV = point.v() - a.v();
        double cross = abU * apV - abV * apU;
        if (Math.abs(cross) > 1.0e-9) {
            return false;
        }
        double dot = apU * abU + apV * abV;
        if (dot < -1.0e-9) {
            return false;
        }
        return dot <= lengthSquared + 1.0e-9;
    }
}