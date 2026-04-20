package com.minicad.geometry2d;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for TrimmedCurve2 class.
 */
class TrimmedCurve2Test {

    @Test
    void trimmedCurvePointAt() {
        Line2 basisCurve = new Line2(new Point2(0, 0), new Direction2(1, 0));

        TrimmedCurve2 trimmed = new TrimmedCurve2(basisCurve, 0.0, 10.0, true);

        Point2 p0 = trimmed.pointAt(0);
        assertEquals(0.0, p0.x(), 1e-10);

        Point2 p1 = trimmed.pointAt(1);
        assertEquals(10.0, p1.x(), 1e-10);

        Point2 p05 = trimmed.pointAt(0.5);
        assertEquals(5.0, p05.x(), 1e-10);
    }

    @Test
    void trimmedCurvePointAtReversed() {
        Line2 basisCurve = new Line2(new Point2(0, 0), new Direction2(1, 0));

        TrimmedCurve2 trimmed = new TrimmedCurve2(basisCurve, 0.0, 10.0, false);

        Point2 p0 = trimmed.pointAt(0);
        assertEquals(10.0, p0.x(), 1e-10);

        Point2 p1 = trimmed.pointAt(1);
        assertEquals(0.0, p1.x(), 1e-10);
    }

    @Test
    void trimmedCurveParameterOnUnderlyingCurveRespectsReverseSense() {
        Line2 basisCurve = new Line2(new Point2(0, 0), new Direction2(1, 0));
        TrimmedCurve2 trimmed = new TrimmedCurve2(basisCurve, 2.0, 8.0, false);

        assertEquals(8.0, trimmed.parameterOnUnderlyingCurve(trimmed.pointAt(0.0)), 1e-10);
        assertEquals(5.0, trimmed.parameterOnUnderlyingCurve(trimmed.pointAt(0.5)), 1e-10);
        assertEquals(2.0, trimmed.parameterOnUnderlyingCurve(trimmed.pointAt(1.0)), 1e-10);
    }

    @Test
    void trimmedCurveContains() {
        Line2 basisCurve = new Line2(new Point2(0, 0), new Direction2(1, 0));

        TrimmedCurve2 trimmed = new TrimmedCurve2(basisCurve, 0.0, 10.0, true);

        assertTrue(trimmed.contains(new Point2(5, 0)));
        assertTrue(trimmed.contains(new Point2(0, 0)));
    }

    @Test
    void trimmedCurveSample() {
        Line2 basisCurve = new Line2(new Point2(0, 0), new Direction2(1, 0));

        TrimmedCurve2 trimmed = new TrimmedCurve2(basisCurve, 0.0, 10.0, true);

        java.util.List<Point2> samples = trimmed.sample(5);
        assertEquals(6, samples.size());
        assertEquals(0.0, samples.get(0).x(), 1e-10);
        assertEquals(10.0, samples.get(5).x(), 1e-10);
    }

    @Test
    void trimmedCurveTangentAt() {
        Line2 basisCurve = new Line2(new Point2(0, 0), new Direction2(1, 0));

        TrimmedCurve2 trimmed = new TrimmedCurve2(basisCurve, 0.0, 10.0, true);

        Vector2 tangent = trimmed.tangentAt(0);
        assertEquals(1.0, tangent.x(), 1e-10);
        assertEquals(0.0, tangent.y(), 1e-10);
    }

    @Test
    void trimmedCurveTangentAtReversed() {
        Line2 basisCurve = new Line2(new Point2(0, 0), new Direction2(1, 0));

        TrimmedCurve2 trimmed = new TrimmedCurve2(basisCurve, 0.0, 10.0, false);

        Vector2 tangent = trimmed.tangentAt(0);
        assertEquals(-1.0, tangent.x(), 1e-10);
        assertEquals(0.0, tangent.y(), 1e-10);
    }

    @Test
    void trimmedCurveBoundingBox() {
        Line2 basisCurve = new Line2(new Point2(0, 0), new Direction2(1, 0));

        TrimmedCurve2 trimmed = new TrimmedCurve2(basisCurve, 2.0, 8.0, true);

        BoundingBox2 box = trimmed.boundingBox();
        assertEquals(2.0, box.minX(), 1e-10);
        assertEquals(0.0, box.minY(), 1e-10);
        assertEquals(8.0, box.maxX(), 1e-10);
        assertEquals(0.0, box.maxY(), 1e-10);
    }

    @Test
    void trimmedCurveLength() {
        Line2 basisCurve = new Line2(new Point2(0, 0), new Direction2(1, 0));

        TrimmedCurve2 trimmed = new TrimmedCurve2(basisCurve, 0.0, 10.0, true);

        assertEquals(10.0, trimmed.length(), 1e-10);
    }

    @Test
    void trimmedCurveClosestPointTo() {
        Line2 basisCurve = new Line2(new Point2(0, 0), new Direction2(1, 0));

        TrimmedCurve2 trimmed = new TrimmedCurve2(basisCurve, 0.0, 10.0, true);

        Point2 onCurve = new Point2(5, 0);
        Point2 closest = trimmed.closestPointTo(onCurve);
        assertEquals(5.0, closest.x(), 1e-10);

        Point2 offCurve = new Point2(5, 10);
        Point2 closestOff = trimmed.closestPointTo(offCurve);
        assertEquals(5.0, closestOff.x(), 1e-10);
        assertEquals(0.0, closestOff.y(), 1e-10);

        Point2 beyond = new Point2(20, 0);
        Point2 closestBeyond = trimmed.closestPointTo(beyond);
        assertEquals(10.0, closestBeyond.x(), 1e-10);
    }

    @Test
    void trimmedCurveDistanceTo() {
        Line2 basisCurve = new Line2(new Point2(0, 0), new Direction2(1, 0));

        TrimmedCurve2 trimmed = new TrimmedCurve2(basisCurve, 0.0, 10.0, true);

        Point2 offCurve = new Point2(5, 10);
        assertEquals(10.0, trimmed.distanceTo(offCurve), 1e-10);
    }

    @Test
    void trimmedCurveMidpoint() {
        Line2 basisCurve = new Line2(new Point2(0, 0), new Direction2(1, 0));

        TrimmedCurve2 trimmed = new TrimmedCurve2(basisCurve, 0.0, 10.0, true);

        Point2 midpoint = trimmed.midpoint();
        assertEquals(5.0, midpoint.x(), 1e-10);
        assertEquals(0.0, midpoint.y(), 1e-10);
    }

    @Test
    void trimmedCurveUnderlyingCurve() {
        Line2 basisCurve = new Line2(new Point2(0, 0), new Direction2(1, 0));

        TrimmedCurve2 trimmed = new TrimmedCurve2(basisCurve, 0.0, 10.0, true);

        assertEquals(basisCurve, trimmed.underlyingCurve());
    }

    @Test
    void trimmedCurveWithCircle() {
        Point2 center = new Point2(0, 0);
        Direction2 xDir = new Direction2(1, 0);
        Circle2 circle = new Circle2(center, xDir, 5.0);

        TrimmedCurve2 trimmed = new TrimmedCurve2(circle, 0.0, Math.PI / 2, true);

        assertEquals(5.0, trimmed.trimStart().x(), 1e-10);
        assertEquals(0.0, trimmed.trimStart().y(), 1e-10);
        assertEquals(0.0, trimmed.trimEnd().x(), 1e-10);
        assertEquals(5.0, trimmed.trimEnd().y(), 1e-10);
    }

    @Test
    void nestedTrimmedCurveResolvesUnderlyingParameter() {
        TrimmedCurve2 inner = new TrimmedCurve2(
                new Line2(new Point2(0, 0), new Direction2(1, 0)),
                0.2,
                0.8,
                false
        );
        TrimmedCurve2 outer = new TrimmedCurve2(inner, 0.25, 0.75, false);

        assertEquals(0.35, outer.parameterOnUnderlyingCurve(outer.pointAt(0.0)), 1e-10);
        assertEquals(0.5, outer.parameterOnUnderlyingCurve(outer.pointAt(0.5)), 1e-10);
        assertEquals(0.65, outer.parameterOnUnderlyingCurve(outer.pointAt(1.0)), 1e-10);
    }
}
