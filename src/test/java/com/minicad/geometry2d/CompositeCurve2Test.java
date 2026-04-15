package com.minicad.geometry2d;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for CompositeCurve2 class.
 */
class CompositeCurve2Test {

    @Test
    void compositeCurveContains() {
        Line2 line = new Line2(new Point2(0, 0), new Direction2(1, 0));
        Circle2 circle = new Circle2(new Point2(10, 0), new Direction2(1, 0), 5.0);

        CompositeCurve2 composite = new CompositeCurve2(List.of(line, circle));

        assertTrue(composite.contains(new Point2(5, 0)));
    }

    @Test
    void compositeCurvePointAt() {
        Line2 line = new Line2(new Point2(0, 0), new Direction2(1, 0));
        Circle2 circle = new Circle2(new Point2(10, 0), new Direction2(1, 0), 5.0);

        CompositeCurve2 composite = new CompositeCurve2(List.of(line, circle));

        Point2 p0 = composite.pointAt(0);
        assertNotNull(p0);
    }

    @Test
    void compositeCurveSample() {
        Line2 line = new Line2(new Point2(0, 0), new Direction2(1, 0));
        Circle2 circle = new Circle2(new Point2(10, 0), new Direction2(1, 0), 5.0);

        CompositeCurve2 composite = new CompositeCurve2(List.of(line, circle));

        List<Point2> samples = composite.sample(4);
        assertTrue(samples.size() > 0);
    }

    @Test
    void compositeCurveBoundingBox() {
        Line2 line = new Line2(new Point2(0, 0), new Direction2(1, 0));
        Circle2 circle = new Circle2(new Point2(10, 0), new Direction2(1, 0), 5.0);

        CompositeCurve2 composite = new CompositeCurve2(List.of(line, circle));

        BoundingBox2 box = composite.boundingBox();
        assertNotNull(box);
    }

    @Test
    void compositeCurveLength() {
        Line2 line = new Line2(new Point2(0, 0), new Direction2(1, 0));
        Circle2 circle = new Circle2(new Point2(10, 0), new Direction2(1, 0), 5.0);

        CompositeCurve2 composite = new CompositeCurve2(List.of(line, circle));

        double length = composite.length();
        assertTrue(length > 0);
    }

    @Test
    void compositeCurveTangentAt() {
        Line2 line = new Line2(new Point2(0, 0), new Direction2(1, 0));
        Circle2 circle = new Circle2(new Point2(10, 0), new Direction2(1, 0), 5.0);

        CompositeCurve2 composite = new CompositeCurve2(List.of(line, circle));

        Vector2 tangent = composite.tangentAt(0);
        assertNotNull(tangent);
    }

    @Test
    void compositeCurveClosestPointTo() {
        Line2 line = new Line2(new Point2(0, 0), new Direction2(1, 0));
        Circle2 circle = new Circle2(new Point2(10, 0), new Direction2(1, 0), 5.0);

        CompositeCurve2 composite = new CompositeCurve2(List.of(line, circle));

        Point2 point = new Point2(5, 0);
        Point2 closest = composite.closestPointTo(point);
        assertNotNull(closest);
    }

    @Test
    void compositeCurveDistanceTo() {
        Line2 line = new Line2(new Point2(0, 0), new Direction2(1, 0));
        Circle2 circle = new Circle2(new Point2(10, 0), new Direction2(1, 0), 5.0);

        CompositeCurve2 composite = new CompositeCurve2(List.of(line, circle));

        Point2 point = new Point2(5, 0);
        double distance = composite.distanceTo(point);
        assertTrue(distance >= 0);
    }

    @Test
    void compositeCurveSegmentCount() {
        Line2 line = new Line2(new Point2(0, 0), new Direction2(1, 0));
        Circle2 circle = new Circle2(new Point2(10, 0), new Direction2(1, 0), 5.0);

        CompositeCurve2 composite = new CompositeCurve2(List.of(line, circle));

        assertEquals(2, composite.segmentCount());
    }

    @Test
    void compositeCurveWithTrimmedCurves() {
        Line2 line = new Line2(new Point2(0, 0), new Direction2(1, 0));
        TrimmedCurve2 trimmed = new TrimmedCurve2(
            line,
            new Point2(0, 0),
            new Point2(10, 0),
            true
        );

        CompositeCurve2 composite = new CompositeCurve2(List.of(trimmed));

        Point2 p0 = composite.pointAt(0);
        assertEquals(0.0, p0.x(), 1e-10);

        Point2 p1 = composite.pointAt(1);
        assertEquals(10.0, p1.x(), 1e-10);
    }

    @Test
    void compositeCurveEmptyThrows() {
        assertThrows(IllegalArgumentException.class, () -> new CompositeCurve2(List.of()));
    }

    @Test
    void compositeCurveWithPolyline() {
        Polyline2 polyline = new Polyline2(List.of(
            new Point2(0, 0),
            new Point2(10, 0),
            new Point2(10, 10)
        ));

        CompositeCurve2 composite = new CompositeCurve2(List.of(polyline));

        assertEquals(1, composite.segmentCount());
        assertNotNull(composite.boundingBox());
    }

    @Test
    void compositeCurveWithEllipse() {
        Point2 center = new Point2(0, 0);
        Direction2 xDir = new Direction2(1, 0);
        Ellipse2 ellipse = new Ellipse2(center, xDir, 4.0, 2.0);

        CompositeCurve2 composite = new CompositeCurve2(List.of(ellipse));

        assertNotNull(composite.pointAt(0));
        assertTrue(composite.length() > 0);
    }
}