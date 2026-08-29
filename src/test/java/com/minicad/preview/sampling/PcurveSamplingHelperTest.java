package com.minicad.preview.sampling;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.minicad.geometry2d.BSplineCurve2;
import com.minicad.geometry2d.Circle2;
import com.minicad.geometry2d.Direction2;
import com.minicad.geometry2d.Ellipse2;
import com.minicad.geometry2d.Line2;
import com.minicad.geometry2d.Parabola2;
import com.minicad.geometry2d.Point2;
import com.minicad.geometry2d.TrimmedCurve2;
import com.minicad.preview.payload.UvPoint;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;

/**
 * Covers PcurveSamplingHelper: snapping, per-curve-type sampling, trimming and scoring helpers.
 */
class PcurveSamplingHelperTest {

    private static final double EPS = 1e-9;

    private static final Point2 ORIGIN = new Point2(0.0, 0.0);
    private static final Direction2 X_DIR = new Direction2(1.0, 0.0);

    private static Line2 line() {
        return new Line2(ORIGIN, X_DIR);
    }

    private static Circle2 circle() {
        return new Circle2(ORIGIN, X_DIR, 3.0);
    }

    private static Ellipse2 ellipse() {
        return new Ellipse2(ORIGIN, X_DIR, 4.0, 2.0);
    }

    /** Degree-1 clamped B-spline from (0,0) to (1,0): knots [0,0,1,1]. */
    private static BSplineCurve2 spline() {
        return new BSplineCurve2(
                1,
                List.of(new Point2(0.0, 0.0), new Point2(1.0, 0.0)),
                List.of(2, 2),
                List.of(0.0, 1.0));
    }

    private static void assertUv(UvPoint expected, UvPoint actual) {
        assertEquals(expected.u(), actual.u(), EPS);
        assertEquals(expected.v(), actual.v(), EPS);
    }

    private static void assertPoint2(Point2 expected, Point2 actual) {
        assertEquals(expected.x(), actual.x(), EPS);
        assertEquals(expected.y(), actual.y(), EPS);
    }

    // ── snapping ───────────────────────────────────────────────────────────

    @Test
    void snapToLineProjectsOntoLine() {
        UvPoint snapped = PcurveSamplingHelper.snapToLine(new UvPoint(2.0, 5.0), line());
        assertUv(new UvPoint(2.0, 0.0), snapped);
    }

    @Test
    void snapToCircleRadiallyProjectsPoint() {
        // outside the circle -> pulled back onto the radius
        assertUv(new UvPoint(3.0, 0.0), PcurveSamplingHelper.snapToCircle(new UvPoint(5.0, 0.0), circle()));
        assertUv(new UvPoint(0.0, -3.0), PcurveSamplingHelper.snapToCircle(new UvPoint(0.0, -9.0), circle()));
        // exactly at the centre -> fallback to parameter 0
        assertUv(new UvPoint(3.0, 0.0), PcurveSamplingHelper.snapToCircle(new UvPoint(0.0, 0.0), circle()));
    }

    @Test
    void snapToEllipseProjectsOntoBoundary() {
        assertUv(new UvPoint(4.0, 0.0), PcurveSamplingHelper.snapToEllipse(new UvPoint(10.0, 0.0), ellipse()));
        assertUv(new UvPoint(0.0, 2.0), PcurveSamplingHelper.snapToEllipse(new UvPoint(0.0, 10.0), ellipse()));
    }

    @Test
    void snapEllipseSeedHandlesCentreAndAxes() {
        // degenerate offset -> parameter 0
        assertPoint2(new Point2(4.0, 0.0), PcurveSamplingHelper.snapEllipseSeed(new UvPoint(0.0, 0.0), ellipse()));
        // along +X -> major-axis end
        assertPoint2(new Point2(4.0, 0.0), PcurveSamplingHelper.snapEllipseSeed(new UvPoint(10.0, 0.0), ellipse()));
        // along +Y -> minor-axis end
        assertPoint2(new Point2(0.0, 2.0), PcurveSamplingHelper.snapEllipseSeed(new UvPoint(0.0, 10.0), ellipse()));
    }

    // ── sampling per curve type ────────────────────────────────────────────

    @Test
    void sampleLinePcurveKeepsEndpointsExact() {
        List<UvPoint> points = PcurveSamplingHelper.sampleLinePcurve(line(), new UvPoint(0.0, 0.0), new UvPoint(5.0, 0.0));
        assertFalse(points.isEmpty());
        assertUv(new UvPoint(0.0, 0.0), points.get(0));
        assertUv(new UvPoint(5.0, 0.0), points.get(points.size() - 1));
        for (UvPoint p : points) {
            assertEquals(0.0, p.v(), EPS, "line lies on v=0");
        }
    }

    @Test
    void sampleCirclePcurveFollowsArc() {
        List<UvPoint> points = PcurveSamplingHelper.sampleCirclePcurve(circle(), new UvPoint(3.0, 0.0), new UvPoint(0.0, 3.0));
        assertFalse(points.isEmpty());
        assertUv(new UvPoint(3.0, 0.0), points.get(0));
        assertUv(new UvPoint(0.0, 3.0), points.get(points.size() - 1));
        for (UvPoint p : points) {
            assertEquals(3.0, Math.hypot(p.u(), p.v()), 1e-6, "arc points stay on the radius");
        }
    }

    @Test
    void sampleEllipsePcurveFollowsArc() {
        List<UvPoint> points = PcurveSamplingHelper.sampleEllipsePcurve(ellipse(), new UvPoint(4.0, 0.0), new UvPoint(0.0, 2.0));
        assertFalse(points.isEmpty());
        assertUv(new UvPoint(4.0, 0.0), points.get(0));
        assertUv(new UvPoint(0.0, 2.0), points.get(points.size() - 1));
        for (UvPoint p : points) {
            // (u/4)^2 + (v/2)^2 == 1 on the ellipse
            double implicit = (p.u() / 4.0) * (p.u() / 4.0) + (p.v() / 2.0) * (p.v() / 2.0);
            assertEquals(1.0, implicit, 1e-6);
        }
    }

    @Test
    void sampleSplinePcurveUsesNearestSamples() {
        List<UvPoint> points = PcurveSamplingHelper.sampleSplinePcurve(spline(), new UvPoint(0.0, 0.0), new UvPoint(1.0, 0.0));
        assertFalse(points.isEmpty());
        assertUv(new UvPoint(0.0, 0.0), points.get(0));
        assertUv(new UvPoint(1.0, 0.0), points.get(points.size() - 1));
    }

    @Test
    void sampleSplinePcurveDegeneratesWhenEndpointsCoincide() {
        // both projections snap to the same sample index -> straight two-point result
        List<UvPoint> points = PcurveSamplingHelper.sampleSplinePcurve(spline(), new UvPoint(0.0, 0.0), new UvPoint(0.0, 0.0));
        assertEquals(2, points.size());
        assertUv(new UvPoint(0.0, 0.0), points.get(0));
        assertUv(new UvPoint(0.0, 0.0), points.get(1));
    }

    @Test
    void sampleTrimmedPcurveHonoursProjection() {
        TrimmedCurve2 trimmed = new TrimmedCurve2(line(), 0.0, 5.0, true);
        UvPoint start = new UvPoint(0.0, 0.0);
        UvPoint end = new UvPoint(5.0, 0.0);
        List<UvPoint> points = PcurveSamplingHelper.sampleTrimmedPcurve(trimmed, start, end);
        assertFalse(points.isEmpty());
        assertUv(start, points.get(0));
        assertUv(end, points.get(points.size() - 1));
    }

    @Test
    void sampleCurve2DispatchesByCurveType() {
        // supported types delegate to their samplers
        assertFalse(PcurveSamplingHelper.sampleCurve2(line(), new UvPoint(0.0, 0.0), new UvPoint(1.0, 0.0)).isEmpty());
        assertFalse(PcurveSamplingHelper.sampleCurve2(circle(), new UvPoint(3.0, 0.0), new UvPoint(0.0, 3.0)).isEmpty());
        assertFalse(PcurveSamplingHelper.sampleCurve2(ellipse(), new UvPoint(4.0, 0.0), new UvPoint(0.0, 2.0)).isEmpty());
        assertFalse(PcurveSamplingHelper.sampleCurve2(spline(), new UvPoint(0.0, 0.0), new UvPoint(1.0, 0.0)).isEmpty());
        assertFalse(PcurveSamplingHelper.sampleCurve2(
                new TrimmedCurve2(line(), 0.0, 5.0, true),
                new UvPoint(0.0, 0.0), new UvPoint(5.0, 0.0)).isEmpty());
        // unsupported pcurve type -> empty
        assertTrue(PcurveSamplingHelper.sampleCurve2(
                new Parabola2(ORIGIN, X_DIR, 1.0),
                new UvPoint(0.0, 0.0), new UvPoint(1.0, 1.0)).isEmpty());
    }

    // ── scoring / alignment ────────────────────────────────────────────────

    @Test
    void scoreIsInfiniteForEmptySamples() {
        assertEquals(Double.POSITIVE_INFINITY,
                PcurveSamplingHelper.score(new UvPoint(0.0, 0.0), new UvPoint(1.0, 0.0), List.of()));
    }

    @Test
    void scoreSumsEndpointDistances() {
        List<UvPoint> samples = List.of(new UvPoint(0.0, 0.0), new UvPoint(1.0, 0.0));
        // start matches the head, end is 2 away from the tail -> 4
        assertEquals(4.0,
                PcurveSamplingHelper.score(new UvPoint(0.0, 0.0), new UvPoint(3.0, 0.0), samples), EPS);
    }

    @Test
    void alignTrimmedSamplesPassesThroughEmptyList() {
        List<UvPoint> empty = List.of();
        assertTrue(PcurveSamplingHelper.alignTrimmedSamples(empty, new UvPoint(0.0, 0.0), new UvPoint(1.0, 0.0)).isEmpty());
    }

    @Test
    void alignTrimmedSamplesKeepsForwardOrder() {
        List<UvPoint> samples = new ArrayList<>(List.of(
                new UvPoint(0.0, 0.0), new UvPoint(1.0, 0.0), new UvPoint(2.0, 0.0)));
        List<UvPoint> aligned = PcurveSamplingHelper.alignTrimmedSamples(samples, new UvPoint(0.0, 0.0), new UvPoint(2.0, 0.0));
        assertEquals(3, aligned.size());
        assertUv(new UvPoint(0.0, 0.0), aligned.get(0));
        assertUv(new UvPoint(2.0, 0.0), aligned.get(2));
    }

    @Test
    void alignTrimmedSamplesReversesWhenBetter() {
        // samples run backwards relative to the projection
        List<UvPoint> samples = new ArrayList<>(List.of(
                new UvPoint(2.0, 0.0), new UvPoint(1.0, 0.0), new UvPoint(0.0, 0.0)));
        List<UvPoint> aligned = PcurveSamplingHelper.alignTrimmedSamples(samples, new UvPoint(0.0, 0.0), new UvPoint(2.0, 0.0));
        assertEquals(3, aligned.size());
        assertUv(new UvPoint(0.0, 0.0), aligned.get(0));
        assertUv(new UvPoint(2.0, 0.0), aligned.get(2));
    }

    // ── small utilities ────────────────────────────────────────────────────

    @Test
    void closestPointIndexFindsNearest() {
        List<Point2> pts = List.of(new Point2(0.0, 0.0), new Point2(5.0, 0.0), new Point2(0.0, 9.0));
        assertEquals(1, PcurveSamplingHelper.closestPointIndex(pts, new UvPoint(4.0, 1.0)));
        assertEquals(2, PcurveSamplingHelper.closestPointIndex(pts, new UvPoint(1.0, 8.0)));
    }

    @Test
    void distanceSquaredAndSameUv() {
        assertEquals(25.0, PcurveSamplingHelper.distanceSquared(new UvPoint(0.0, 0.0), new UvPoint(3.0, 4.0)), EPS);
        assertTrue(PcurveSamplingHelper.sameUv(new UvPoint(1.0, 2.0), new UvPoint(1.0, 2.0)));
        assertFalse(PcurveSamplingHelper.sameUv(new UvPoint(1.0, 2.0), new UvPoint(1.0, 3.0)));
    }
}
