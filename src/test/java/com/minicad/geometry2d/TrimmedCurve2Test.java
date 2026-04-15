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
        Point2 trimStart = new Point2(0, 0);
        Point2 trimEnd = new Point2(10, 0);

        TrimmedCurve2 trimmed = new TrimmedCurve2(basisCurve, trimStart, trimEnd, true);

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
        Point2 trimStart = new Point2(0, 0);
        Point2 trimEnd = new Point2(10, 0);

        TrimmedCurve2 trimmed = new TrimmedCurve2(basisCurve, trimStart, trimEnd, false);

        Point2 p0 = trimmed.pointAt(0);
        assertEquals(10.0, p0.x(), 1e-10);

        Point2 p1 = trimmed.pointAt(1);
        assertEquals(0.0, p1.x(), 1e-10);
    }

    @Test
    void trimmedCurveContains() {
        Line2 basisCurve = new Line2(new Point2(0, 0), new Direction2(1, 0));
        Point2 trimStart = new Point2(0, 0);
        Point2 trimEnd = new Point2(10, 0);

        TrimmedCurve2 trimmed = new TrimmedCurve2(basisCurve, trimStart, trimEnd, true);

        assertTrue(trimmed.contains(new Point2(5, 0)));
        assertTrue(trimmed.contains(new Point2(0, 0)));
    }

    @Test
    void trimmedCurveSample() {
        Line2 basisCurve = new Line2(new Point2(0, 0), new Direction2(1, 0));
        Point2 trimStart = new Point2(0, 0);
        Point2 trimEnd = new Point2(10, 0);

        TrimmedCurve2 trimmed = new TrimmedCurve2(basisCurve, trimStart, trimEnd, true);

        java.util.List<Point2> samples = trimmed.sample(5);
        assertEquals(6, samples.size());
        assertEquals(0.0, samples.get(0).x(), 1e-10);
        assertEquals(10.0, samples.get(5).x(), 1e-10);
    }

    @Test
    void trimmedCurveTangentAt() {
        Line2 basisCurve = new Line2(new Point2(0, 0), new Direction2(1, 0));
        Point2 trimStart = new Point2(0, 0);
        Point2 trimEnd = new Point2(10, 0);

        TrimmedCurve2 trimmed = new TrimmedCurve2(basisCurve, trimStart, trimEnd, true);

        Vector2 tangent = trimmed.tangentAt(0);
        assertEquals(1.0, tangent.x(), 1e-10);
        assertEquals(0.0, tangent.y(), 1e-10);
    }

    @Test
    void trimmedCurveTangentAtReversed() {
        Line2 basisCurve = new Line2(new Point2(0, 0), new Direction2(1, 0));
        Point2 trimStart = new Point2(0, 0);
        Point2 trimEnd = new Point2(10, 0);

        TrimmedCurve2 trimmed = new TrimmedCurve2(basisCurve, trimStart, trimEnd, false);

        Vector2 tangent = trimmed.tangentAt(0);
        assertEquals(-1.0, tangent.x(), 1e-10);
        assertEquals(0.0, tangent.y(), 1e-10);
    }

    @Test
    void trimmedCurveBoundingBox() {
        Line2 basisCurve = new Line2(new Point2(0, 0), new Direction2(1, 0));
        Point2 trimStart = new Point2(2, 3);
        Point2 trimEnd = new Point2(8, 7);

        TrimmedCurve2 trimmed = new TrimmedCurve2(basisCurve, trimStart, trimEnd, true);

        BoundingBox2 box = trimmed.boundingBox();
        assertEquals(2.0, box.minX(), 1e-10);
        assertEquals(3.0, box.minY(), 1e-10);
        assertEquals(8.0, box.maxX(), 1e-10);
        assertEquals(7.0, box.maxY(), 1e-10);
    }

    @Test
    void trimmedCurveLength() {
        Line2 basisCurve = new Line2(new Point2(0, 0), new Direction2(1, 0));
        Point2 trimStart = new Point2(0, 0);
        Point2 trimEnd = new Point2(10, 0);

        TrimmedCurve2 trimmed = new TrimmedCurve2(basisCurve, trimStart, trimEnd, true);

        assertEquals(10.0, trimmed.length(), 1e-10);
    }

    @Test
    void trimmedCurveClosestPointTo() {
        Line2 basisCurve = new Line2(new Point2(0, 0), new Direction2(1, 0));
        Point2 trimStart = new Point2(0, 0);
        Point2 trimEnd = new Point2(10, 0);

        TrimmedCurve2 trimmed = new TrimmedCurve2(basisCurve, trimStart, trimEnd, true);

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
        Point2 trimStart = new Point2(0, 0);
        Point2 trimEnd = new Point2(10, 0);

        TrimmedCurve2 trimmed = new TrimmedCurve2(basisCurve, trimStart, trimEnd, true);

        Point2 offCurve = new Point2(5, 10);
        assertEquals(10.0, trimmed.distanceTo(offCurve), 1e-10);
    }

    @Test
    void trimmedCurveMidpoint() {
        Line2 basisCurve = new Line2(new Point2(0, 0), new Direction2(1, 0));
        Point2 trimStart = new Point2(0, 0);
        Point2 trimEnd = new Point2(10, 0);

        TrimmedCurve2 trimmed = new TrimmedCurve2(basisCurve, trimStart, trimEnd, true);

        Point2 midpoint = trimmed.midpoint();
        assertEquals(5.0, midpoint.x(), 1e-10);
        assertEquals(0.0, midpoint.y(), 1e-10);
    }

    @Test
    void trimmedCurveUnderlyingCurve() {
        Line2 basisCurve = new Line2(new Point2(0, 0), new Direction2(1, 0));
        Point2 trimStart = new Point2(0, 0);
        Point2 trimEnd = new Point2(10, 0);

        TrimmedCurve2 trimmed = new TrimmedCurve2(basisCurve, trimStart, trimEnd, true);

        assertEquals(basisCurve, trimmed.underlyingCurve());
    }

    @Test
    void trimmedCurveWithCircle() {
        Point2 center = new Point2(0, 0);
        Direction2 xDir = new Direction2(1, 0);
        Circle2 circle = new Circle2(center, xDir, 5.0);
        Point2 trimStart = circle.pointAt(0);
        Point2 trimEnd = circle.pointAt(Math.PI / 2);

        TrimmedCurve2 trimmed = new TrimmedCurve2(circle, trimStart, trimEnd, true);

        assertEquals(5.0, trimmed.trimStart().x(), 1e-10);
        assertEquals(0.0, trimmed.trimStart().y(), 1e-10);
    }
}