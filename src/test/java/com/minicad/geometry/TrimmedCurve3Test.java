package com.minicad.geometry;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for TrimmedCurve3 class.
 * TrimmedCurve3 stores parameter values on the basis curve, not geometric points.
 */
class TrimmedCurve3Test {

    @Test
    void trimmedCurvePointAt() {
        Line3 line = new Line3(new CartesianPoint(0, 0, 0), new Direction3(1, 0, 0));
        // Line3.pointAt(t) = origin + t * direction; params 0 and 10 correspond to (0,0,0) and (10,0,0)
        TrimmedCurve3 trimmed = new TrimmedCurve3(line, 0.0, 10.0, true);

        CartesianPoint p0 = trimmed.pointAt(0);
        assertEquals(0.0, p0.x(), 1e-10);

        CartesianPoint p1 = trimmed.pointAt(1);
        assertEquals(10.0, p1.x(), 1e-10);

        CartesianPoint p05 = trimmed.pointAt(0.5);
        assertEquals(5.0, p05.x(), 1e-10);
    }

    @Test
    void trimmedCurvePointAtReversed() {
        Line3 line = new Line3(new CartesianPoint(0, 0, 0), new Direction3(1, 0, 0));
        TrimmedCurve3 trimmed = new TrimmedCurve3(line, 0.0, 10.0, false);

        CartesianPoint p0 = trimmed.pointAt(0);
        assertEquals(10.0, p0.x(), 1e-10);

        CartesianPoint p1 = trimmed.pointAt(1);
        assertEquals(0.0, p1.x(), 1e-10);
    }

    @Test
    void trimmedCurveParameterAtReversed() {
        Line3 line = new Line3(new CartesianPoint(0, 0, 0), new Direction3(1, 0, 0));
        TrimmedCurve3 trimmed = new TrimmedCurve3(line, 0.0, 10.0, false);

        assertEquals(0.0, trimmed.parameterAt(new CartesianPoint(10, 0, 0)), 1e-10);
        assertEquals(0.5, trimmed.parameterAt(new CartesianPoint(5, 0, 0)), 1e-10);
        assertEquals(1.0, trimmed.parameterAt(new CartesianPoint(0, 0, 0)), 1e-10);
    }

    @Test
    void trimmedCurveContains() {
        Line3 line = new Line3(new CartesianPoint(0, 0, 0), new Direction3(1, 0, 0));
        TrimmedCurve3 trimmed = new TrimmedCurve3(line, 0.0, 10.0, true);

        assertTrue(trimmed.contains(new CartesianPoint(5, 0, 0)));
        assertTrue(trimmed.contains(new CartesianPoint(0, 0, 0)));
        assertFalse(trimmed.contains(new CartesianPoint(20, 0, 0)));
        assertFalse(trimmed.contains(new CartesianPoint(5, 1, 0)));
    }

    @Test
    void trimmedCurveSample() {
        Line3 line = new Line3(new CartesianPoint(0, 0, 0), new Direction3(1, 0, 0));
        TrimmedCurve3 trimmed = new TrimmedCurve3(line, 0.0, 10.0, true);

        java.util.List<CartesianPoint> samples = trimmed.sample(5);
        assertEquals(6, samples.size());
        assertEquals(0.0, samples.get(0).x(), 1e-10);
        assertEquals(10.0, samples.get(5).x(), 1e-10);
    }

    @Test
    void trimmedCurveTangentAt() {
        Line3 line = new Line3(new CartesianPoint(0, 0, 0), new Direction3(1, 0, 0));
        TrimmedCurve3 trimmed = new TrimmedCurve3(line, 0.0, 10.0, true);

        Vector3 tangent = trimmed.tangentAt(0);
        assertEquals(1.0, tangent.x(), 1e-10);
        assertEquals(0.0, tangent.y(), 1e-10);
    }

    @Test
    void trimmedCurveTangentAtReversed() {
        Line3 line = new Line3(new CartesianPoint(0, 0, 0), new Direction3(1, 0, 0));
        TrimmedCurve3 trimmed = new TrimmedCurve3(line, 0.0, 10.0, false);

        Vector3 tangent = trimmed.tangentAt(0);
        assertEquals(-1.0, tangent.x(), 1e-10);
        assertEquals(0.0, tangent.y(), 1e-10);
    }

    @Test
    void trimmedCurveBoundingBox() {
        Line3 line = new Line3(new CartesianPoint(2, 3, 5), new Direction3(1, 0, 0));
        TrimmedCurve3 trimmed = new TrimmedCurve3(line, 0.0, 6.0, true);
        // pointAt(0) = (2,3,5), pointAt(1) = (8,3,5)

        BoundingBox3 box = trimmed.boundingBox();
        assertEquals(2.0, box.minX(), 1e-10);
        assertEquals(3.0, box.minY(), 1e-10);
        assertEquals(5.0, box.minZ(), 1e-10);
        assertEquals(8.0, box.maxX(), 1e-10);
    }

    @Test
    void trimmedCurveLength() {
        Line3 line = new Line3(new CartesianPoint(0, 0, 0), new Direction3(1, 0, 0));
        TrimmedCurve3 trimmed = new TrimmedCurve3(line, 0.0, 10.0, true);

        assertEquals(10.0, trimmed.length(), 1e-10);
    }

    @Test
    void trimmedCurveClosestPointTo() {
        Line3 line = new Line3(new CartesianPoint(0, 0, 0), new Direction3(1, 0, 0));
        TrimmedCurve3 trimmed = new TrimmedCurve3(line, 0.0, 10.0, true);

        CartesianPoint onCurve = new CartesianPoint(5, 0, 0);
        CartesianPoint closest = trimmed.closestPointTo(onCurve);
        assertEquals(5.0, closest.x(), 0.1);

        CartesianPoint offCurve = new CartesianPoint(5, 10, 0);
        CartesianPoint closestOff = trimmed.closestPointTo(offCurve);
        assertEquals(5.0, closestOff.x(), 0.1);
        assertEquals(0.0, closestOff.y(), 0.1);

        CartesianPoint beyond = new CartesianPoint(20, 0, 0);
        CartesianPoint closestBeyond = trimmed.closestPointTo(beyond);
        assertTrue(closestBeyond.x() >= 9.5 && closestBeyond.x() <= 10.5);
    }

    @Test
    void trimmedCurveDistanceTo() {
        Line3 line = new Line3(new CartesianPoint(0, 0, 0), new Direction3(1, 0, 0));
        TrimmedCurve3 trimmed = new TrimmedCurve3(line, 0.0, 10.0, true);

        CartesianPoint offCurve = new CartesianPoint(5, 10, 0);
        double distance = trimmed.distanceTo(offCurve);
        assertEquals(10.0, distance, 0.1);
    }

    @Test
    void trimmedCurveMidpoint() {
        Line3 line = new Line3(new CartesianPoint(0, 0, 0), new Direction3(1, 0, 0));
        TrimmedCurve3 trimmed = new TrimmedCurve3(line, 0.0, 10.0, true);

        CartesianPoint midpoint = trimmed.midpoint();
        assertEquals(5.0, midpoint.x(), 1e-10);
        assertEquals(0.0, midpoint.y(), 1e-10);
    }

    @Test
    void trimmedCurveCurvatureLine() {
        Line3 line = new Line3(new CartesianPoint(0, 0, 0), new Direction3(1, 0, 0));
        TrimmedCurve3 trimmed = new TrimmedCurve3(line, 0.0, 10.0, true);

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
        // Circle.pointAt(t) = t is the angle
        TrimmedCurve3 trimmed = new TrimmedCurve3(circle, 0.0, Math.PI / 2, true);

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
        TrimmedCurve3 trimmed = new TrimmedCurve3(circle, 0.0, Math.PI / 2, true);

        CartesianPoint start = trimmed.pointAt(0);
        assertEquals(5.0, start.x(), 1e-10);
        assertEquals(0.0, start.y(), 1e-10);

        CartesianPoint end = trimmed.pointAt(1);
        assertEquals(0.0, end.x(), 1e-10);
        assertEquals(5.0, end.y(), 1e-10);

        BoundingBox3 box = trimmed.boundingBox();
        assertTrue(box.contains(new CartesianPoint(5, 0, 0)));
        assertTrue(box.contains(new CartesianPoint(0, 5, 0)));
    }

    @Test
    void trimmedSurfaceCompositeParameterAtRespectsSense() {
        SurfaceCurve3 surfaceCurve = new SurfaceCurve3(new CompositeCurve3(java.util.List.of(
                new Line3(
                        new CartesianPoint(0, 0, 0),
                        new Direction3(1, 0, 0)
                ),
                new Line3(
                        new CartesianPoint(1, 0, 0),
                        new Direction3(0, 0, 1)
                )
        )));
        TrimmedCurve3 trimmed = new TrimmedCurve3(surfaceCurve, 0.25, 0.75, false);

        assertEquals(0.0, trimmed.parameterAt(trimmed.pointAt(0.0)), 1e-10);
        assertEquals(0.5, trimmed.parameterAt(trimmed.pointAt(0.5)), 1e-10);
        assertEquals(1.0, trimmed.parameterAt(trimmed.pointAt(1.0)), 1e-10);
    }
}
