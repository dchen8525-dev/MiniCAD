package com.minicad.geometry;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for TrimmedCurve3 class.
 */
class TrimmedCurve3Test {

    @Test
    void trimmedCurvePointAt() {
        Line3 line = new Line3(new CartesianPoint(0, 0, 0), new Direction3(1, 0, 0));
        CartesianPoint trimStart = new CartesianPoint(0, 0, 0);
        CartesianPoint trimEnd = new CartesianPoint(10, 0, 0);

        TrimmedCurve3 trimmed = new TrimmedCurve3(line, trimStart, trimEnd, true);

        // Point at parameter=0 should be trim start
        CartesianPoint p0 = trimmed.pointAt(0);
        assertEquals(0.0, p0.x(), 1e-10);
        assertEquals(0.0, p0.y(), 1e-10);

        // Point at parameter=1 should be trim end
        CartesianPoint p1 = trimmed.pointAt(1);
        assertEquals(10.0, p1.x(), 1e-10);
        assertEquals(0.0, p1.y(), 1e-10);

        // Point at parameter=0.5 should be midpoint
        CartesianPoint p05 = trimmed.pointAt(0.5);
        assertEquals(5.0, p05.x(), 1e-10);
        assertEquals(0.0, p05.y(), 1e-10);
    }

    @Test
    void trimmedCurvePointAtReversed() {
        Line3 line = new Line3(new CartesianPoint(0, 0, 0), new Direction3(1, 0, 0));
        CartesianPoint trimStart = new CartesianPoint(0, 0, 0);
        CartesianPoint trimEnd = new CartesianPoint(10, 0, 0);

        // Reversed sense - starts at trimEnd, ends at trimStart
        TrimmedCurve3 trimmed = new TrimmedCurve3(line, trimStart, trimEnd, false);

        CartesianPoint p0 = trimmed.pointAt(0);
        assertEquals(10.0, p0.x(), 1e-10);

        CartesianPoint p1 = trimmed.pointAt(1);
        assertEquals(0.0, p1.x(), 1e-10);
    }

    @Test
    void trimmedCurveContains() {
        Line3 line = new Line3(new CartesianPoint(0, 0, 0), new Direction3(1, 0, 0));
        CartesianPoint trimStart = new CartesianPoint(0, 0, 0);
        CartesianPoint trimEnd = new CartesianPoint(10, 0, 0);

        TrimmedCurve3 trimmed = new TrimmedCurve3(line, trimStart, trimEnd, true);

        // Points on the underlying line
        assertTrue(trimmed.contains(new CartesianPoint(5, 0, 0)));
        assertTrue(trimmed.contains(new CartesianPoint(0, 0, 0)));
    }

    @Test
    void trimmedCurveSample() {
        Line3 line = new Line3(new CartesianPoint(0, 0, 0), new Direction3(1, 0, 0));
        CartesianPoint trimStart = new CartesianPoint(0, 0, 0);
        CartesianPoint trimEnd = new CartesianPoint(10, 0, 0);

        TrimmedCurve3 trimmed = new TrimmedCurve3(line, trimStart, trimEnd, true);

        java.util.List<CartesianPoint> samples = trimmed.sample(5);
        assertEquals(6, samples.size());
        assertEquals(0.0, samples.get(0).x(), 1e-10);
        assertEquals(10.0, samples.get(5).x(), 1e-10);
    }

    @Test
    void trimmedCurveTangentAt() {
        Line3 line = new Line3(new CartesianPoint(0, 0, 0), new Direction3(1, 0, 0));
        CartesianPoint trimStart = new CartesianPoint(0, 0, 0);
        CartesianPoint trimEnd = new CartesianPoint(10, 0, 0);

        TrimmedCurve3 trimmed = new TrimmedCurve3(line, trimStart, trimEnd, true);

        Vector3 tangent = trimmed.tangentAt(0);
        assertEquals(1.0, tangent.x(), 1e-10);
        assertEquals(0.0, tangent.y(), 1e-10);
    }

    @Test
    void trimmedCurveTangentAtReversed() {
        Line3 line = new Line3(new CartesianPoint(0, 0, 0), new Direction3(1, 0, 0));
        CartesianPoint trimStart = new CartesianPoint(0, 0, 0);
        CartesianPoint trimEnd = new CartesianPoint(10, 0, 0);

        TrimmedCurve3 trimmed = new TrimmedCurve3(line, trimStart, trimEnd, false);

        Vector3 tangent = trimmed.tangentAt(0);
        assertEquals(-1.0, tangent.x(), 1e-10);
        assertEquals(0.0, tangent.y(), 1e-10);
    }

    @Test
    void trimmedCurveBoundingBox() {
        Line3 line = new Line3(new CartesianPoint(0, 0, 0), new Direction3(1, 0, 0));
        CartesianPoint trimStart = new CartesianPoint(2, 3, 5);
        CartesianPoint trimEnd = new CartesianPoint(8, 7, 10);

        TrimmedCurve3 trimmed = new TrimmedCurve3(line, trimStart, trimEnd, true);

        BoundingBox3 box = trimmed.boundingBox();
        assertEquals(2.0, box.minX(), 1e-10);
        assertEquals(3.0, box.minY(), 1e-10);
        assertEquals(5.0, box.minZ(), 1e-10);
        assertEquals(8.0, box.maxX(), 1e-10);
        assertEquals(7.0, box.maxY(), 1e-10);
        assertEquals(10.0, box.maxZ(), 1e-10);
    }

    @Test
    void trimmedCurveLength() {
        Line3 line = new Line3(new CartesianPoint(0, 0, 0), new Direction3(1, 0, 0));
        CartesianPoint trimStart = new CartesianPoint(0, 0, 0);
        CartesianPoint trimEnd = new CartesianPoint(10, 0, 0);

        TrimmedCurve3 trimmed = new TrimmedCurve3(line, trimStart, trimEnd, true);

        assertEquals(10.0, trimmed.length(), 1e-10);
    }

    @Test
    void trimmedCurveClosestPointTo() {
        Line3 line = new Line3(new CartesianPoint(0, 0, 0), new Direction3(1, 0, 0));
        CartesianPoint trimStart = new CartesianPoint(0, 0, 0);
        CartesianPoint trimEnd = new CartesianPoint(10, 0, 0);

        TrimmedCurve3 trimmed = new TrimmedCurve3(line, trimStart, trimEnd, true);

        // Point on the trimmed curve
        CartesianPoint onCurve = new CartesianPoint(5, 0, 0);
        CartesianPoint closest = trimmed.closestPointTo(onCurve);
        assertEquals(5.0, closest.x(), 1e-10);

        // Point off the curve
        CartesianPoint offCurve = new CartesianPoint(5, 10, 0);
        CartesianPoint closestOff = trimmed.closestPointTo(offCurve);
        assertEquals(5.0, closestOff.x(), 1e-10);
        assertEquals(0.0, closestOff.y(), 1e-10);

        // Point beyond trim end
        CartesianPoint beyond = new CartesianPoint(20, 0, 0);
        CartesianPoint closestBeyond = trimmed.closestPointTo(beyond);
        assertEquals(10.0, closestBeyond.x(), 1e-10);  // Clamped to trim end
    }

    @Test
    void trimmedCurveDistanceTo() {
        Line3 line = new Line3(new CartesianPoint(0, 0, 0), new Direction3(1, 0, 0));
        CartesianPoint trimStart = new CartesianPoint(0, 0, 0);
        CartesianPoint trimEnd = new CartesianPoint(10, 0, 0);

        TrimmedCurve3 trimmed = new TrimmedCurve3(line, trimStart, trimEnd, true);

        CartesianPoint offCurve = new CartesianPoint(5, 10, 0);
        double distance = trimmed.distanceTo(offCurve);
        assertEquals(10.0, distance, 1e-10);
    }

    @Test
    void trimmedCurveMidpoint() {
        Line3 line = new Line3(new CartesianPoint(0, 0, 0), new Direction3(1, 0, 0));
        CartesianPoint trimStart = new CartesianPoint(0, 0, 0);
        CartesianPoint trimEnd = new CartesianPoint(10, 0, 0);

        TrimmedCurve3 trimmed = new TrimmedCurve3(line, trimStart, trimEnd, true);

        CartesianPoint midpoint = trimmed.midpoint();
        assertEquals(5.0, midpoint.x(), 1e-10);
        assertEquals(0.0, midpoint.y(), 1e-10);
    }

    @Test
    void trimmedCurveCurvatureLine() {
        Line3 line = new Line3(new CartesianPoint(0, 0, 0), new Direction3(1, 0, 0));
        CartesianPoint trimStart = new CartesianPoint(0, 0, 0);
        CartesianPoint trimEnd = new CartesianPoint(10, 0, 0);

        TrimmedCurve3 trimmed = new TrimmedCurve3(line, trimStart, trimEnd, true);

        assertEquals(0.0, trimmed.curvature(), 1e-10);
        assertEquals(0.0, trimmed.curvatureAt(0.5), 1e-10);
    }

    @Test
    void trimmedCurveCurvatureCircle() {
        Axis2Placement3D position = new Axis2Placement3D(
            new CartesianPoint(0, 0, 0),
            new Direction3(0, 0, 1),
            new Direction3(1, 0, 0)
        );
        Circle circle = new Circle(position, 5.0);
        CartesianPoint trimStart = circle.pointAt(0);
        CartesianPoint trimEnd = circle.pointAt(Math.PI / 2);

        TrimmedCurve3 trimmed = new TrimmedCurve3(circle, trimStart, trimEnd, true);

        // Curvature of circle is 1/radius
        assertEquals(0.2, trimmed.curvature(), 1e-10);
    }

    @Test
    void trimmedCurveArc() {
        Axis2Placement3D position = new Axis2Placement3D(
            new CartesianPoint(0, 0, 0),
            new Direction3(0, 0, 1),
            new Direction3(1, 0, 0)
        );
        Circle circle = new Circle(position, 5.0);
        CartesianPoint trimStart = circle.pointAt(0);      // (5, 0, 0)
        CartesianPoint trimEnd = circle.pointAt(Math.PI / 2);  // (0, 5, 0)

        TrimmedCurve3 trimmed = new TrimmedCurve3(circle, trimStart, trimEnd, true);

        // Bounding box should enclose the arc
        BoundingBox3 box = trimmed.boundingBox();
        assertTrue(box.contains(new CartesianPoint(5, 0, 0)));
        assertTrue(box.contains(new CartesianPoint(0, 5, 0)));
    }
}