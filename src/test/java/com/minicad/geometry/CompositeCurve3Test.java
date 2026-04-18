package com.minicad.geometry;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for CompositeCurve3 class.
 */
class CompositeCurve3Test {

    @Test
    void compositeCurveContains() {
        Line3 line = new Line3(new CartesianPoint(0, 0, 0), new Direction3(1, 0, 0));
        Circle circle = new Circle(new Axis2Placement3D(
            new CartesianPoint(10, 0, 0),
            new Direction3(0, 0, 1),
            new Direction3(1, 0, 0)
        ), 5.0);

        CompositeCurve3 composite = new CompositeCurve3(List.of(line, circle));

        assertTrue(composite.contains(new CartesianPoint(5, 0, 0)));
    }

    @Test
    void compositeCurvePointAt() {
        Line3 line = new Line3(new CartesianPoint(0, 0, 0), new Direction3(1, 0, 0));
        Circle circle = new Circle(new Axis2Placement3D(
            new CartesianPoint(10, 0, 0),
            new Direction3(0, 0, 1),
            new Direction3(1, 0, 0)
        ), 5.0);

        CompositeCurve3 composite = new CompositeCurve3(List.of(line, circle));

        // Point at parameter=0 should be on first segment
        CartesianPoint p0 = composite.pointAt(0);
        // First segment starts at origin
        assertNotNull(p0);
    }

    @Test
    void compositeCurveSample() {
        Line3 line = new Line3(new CartesianPoint(0, 0, 0), new Direction3(1, 0, 0));
        Circle circle = new Circle(new Axis2Placement3D(
            new CartesianPoint(10, 0, 0),
            new Direction3(0, 0, 1),
            new Direction3(1, 0, 0)
        ), 5.0);

        CompositeCurve3 composite = new CompositeCurve3(List.of(line, circle));

        List<CartesianPoint> samples = composite.sample(4);
        assertTrue(samples.size() > 0);
    }

    @Test
    void compositeCurveBoundingBox() {
        Line3 line = new Line3(new CartesianPoint(0, 0, 0), new Direction3(1, 0, 0));
        Circle circle = new Circle(new Axis2Placement3D(
            new CartesianPoint(10, 0, 0),
            new Direction3(0, 0, 1),
            new Direction3(1, 0, 0)
        ), 5.0);

        CompositeCurve3 composite = new CompositeCurve3(List.of(line, circle));

        BoundingBox3 box = composite.boundingBox();
        assertNotNull(box);
    }

    @Test
    void compositeCurveLength() {
        Line3 line = new Line3(new CartesianPoint(0, 0, 0), new Direction3(1, 0, 0));
        Circle circle = new Circle(new Axis2Placement3D(
            new CartesianPoint(10, 0, 0),
            new Direction3(0, 0, 1),
            new Direction3(1, 0, 0)
        ), 5.0);

        CompositeCurve3 composite = new CompositeCurve3(List.of(line, circle));

        double length = composite.length();
        assertTrue(length > 0);
    }

    @Test
    void compositeCurveTangentAt() {
        Line3 line = new Line3(new CartesianPoint(0, 0, 0), new Direction3(1, 0, 0));
        Circle circle = new Circle(new Axis2Placement3D(
            new CartesianPoint(10, 0, 0),
            new Direction3(0, 0, 1),
            new Direction3(1, 0, 0)
        ), 5.0);

        CompositeCurve3 composite = new CompositeCurve3(List.of(line, circle));

        Vector3 tangent = composite.tangentAt(0);
        assertNotNull(tangent);
    }

    @Test
    void compositeCurveClosestPointTo() {
        Line3 line = new Line3(new CartesianPoint(0, 0, 0), new Direction3(1, 0, 0));
        Circle circle = new Circle(new Axis2Placement3D(
            new CartesianPoint(10, 0, 0),
            new Direction3(0, 0, 1),
            new Direction3(1, 0, 0)
        ), 5.0);

        CompositeCurve3 composite = new CompositeCurve3(List.of(line, circle));

        CartesianPoint point = new CartesianPoint(5, 0, 0);
        CartesianPoint closest = composite.closestPointTo(point);
        assertNotNull(closest);
    }

    @Test
    void compositeCurveDistanceTo() {
        Line3 line = new Line3(new CartesianPoint(0, 0, 0), new Direction3(1, 0, 0));
        Circle circle = new Circle(new Axis2Placement3D(
            new CartesianPoint(10, 0, 0),
            new Direction3(0, 0, 1),
            new Direction3(1, 0, 0)
        ), 5.0);

        CompositeCurve3 composite = new CompositeCurve3(List.of(line, circle));

        CartesianPoint point = new CartesianPoint(5, 0, 0);
        double distance = composite.distanceTo(point);
        assertTrue(distance >= 0);
    }

    @Test
    void compositeCurveSegmentCount() {
        Line3 line = new Line3(new CartesianPoint(0, 0, 0), new Direction3(1, 0, 0));
        Circle circle = new Circle(new Axis2Placement3D(
            new CartesianPoint(10, 0, 0),
            new Direction3(0, 0, 1),
            new Direction3(1, 0, 0)
        ), 5.0);

        CompositeCurve3 composite = new CompositeCurve3(List.of(line, circle));

        assertEquals(2, composite.segmentCount());
    }

    @Test
    void compositeCurveWithTrimmedCurves() {
        Line3 line = new Line3(new CartesianPoint(0, 0, 0), new Direction3(1, 0, 0));
        TrimmedCurve3 trimmed = new TrimmedCurve3(
            line,
            0.0,
            10.0,
            true
        );

        CompositeCurve3 composite = new CompositeCurve3(List.of(trimmed));

        CartesianPoint p0 = composite.pointAt(0);
        assertEquals(0.0, p0.x(), 1e-10);

        CartesianPoint p1 = composite.pointAt(1);
        assertEquals(10.0, p1.x(), 1e-10);
    }

    @Test
    void compositeCurveEmptyThrows() {
        assertThrows(IllegalArgumentException.class, () -> new CompositeCurve3(List.of()));
    }

    @Test
    void compositeCurveWithPolyline() {
        Polyline3 polyline = new Polyline3(List.of(
            new CartesianPoint(0, 0, 0),
            new CartesianPoint(10, 0, 0),
            new CartesianPoint(10, 10, 0)
        ));

        CompositeCurve3 composite = new CompositeCurve3(List.of(polyline));

        assertEquals(1, composite.segmentCount());
        assertNotNull(composite.boundingBox());
    }

    @Test
    void compositeCurveWithEllipse() {
        Ellipse3 ellipse = new Ellipse3(new Axis2Placement3D(
            new CartesianPoint(0, 0, 0),
            new Direction3(0, 0, 1),
            new Direction3(1, 0, 0)
        ), 4.0, 2.0);

        CompositeCurve3 composite = new CompositeCurve3(List.of(ellipse));

        assertNotNull(composite.pointAt(0));
        assertTrue(composite.length() > 0);
    }
}