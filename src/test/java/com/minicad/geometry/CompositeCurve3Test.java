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

    @Test
    void compositeCurveMapsNormalizedParameterIntoBsplineDomain() {
        BSplineCurve3 bspline = new BSplineCurve3(
            1,
            List.of(
                new CartesianPoint(0, 0, 0),
                new CartesianPoint(1, 0, 0),
                new CartesianPoint(2, 0, 0)
            ),
            List.of(2, 1, 2),
            List.of(0.0, 1.0, 2.0)
        );

        CompositeCurve3 composite = new CompositeCurve3(List.of(bspline));

        CartesianPoint midpoint = composite.pointAt(0.5);
        assertEquals(1.0, midpoint.x(), 1e-10);
        assertEquals(0.5, composite.parameterAt(new CartesianPoint(1.0, 0.0, 0.0)), 1e-10);
    }

    @Test
    void compositeCurveMapsNormalizedParameterIntoRationalBsplineDomain() {
        RationalBSplineCurve3 rational = new RationalBSplineCurve3(
            1,
            List.of(
                new CartesianPoint(0, 0, 0),
                new CartesianPoint(1, 0, 0),
                new CartesianPoint(2, 0, 0)
            ),
            List.of(1.0, 1.0, 1.0),
            List.of(2, 1, 2),
            List.of(0.0, 1.0, 2.0)
        );

        CompositeCurve3 composite = new CompositeCurve3(List.of(rational));

        // The rational twin has the same [0, 2] knot domain as the plain spline,
        // and gets the same exact basis-to-normalised inverse instead of sampling.
        assertEquals(1.0, composite.pointAt(0.5).x(), 1e-10);
        assertEquals(0.5, composite.parameterAt(new CartesianPoint(1.0, 0.0, 0.0)), 1e-10);
    }

    @Test
    void compositeCurveMapsNormalizedParameterIntoBsplineTangent() {
        List<CartesianPoint> controlPoints = List.of(
                new CartesianPoint(0, 0, 0), new CartesianPoint(1, 1, 0), new CartesianPoint(2, 0, 0));
        List<Curve3> splines = List.of(
                new BSplineCurve3(1, controlPoints, List.of(2, 1, 2), List.of(0.0, 1.0, 2.0)),
                new RationalBSplineCurve3(
                        1, controlPoints, List.of(1.0, 1.0, 1.0), List.of(2, 1, 2), List.of(0.0, 1.0, 2.0)));

        for (Curve3 spline : splines) {
            CompositeCurve3 composite = new CompositeCurve3(List.of(spline));
            for (double parameter : new double[] {0.25, 0.5, 0.75}) {
                // The geometric tangent of the composite, taken from its own pointAt,
                // is the oracle: tangentAt must not fall back to the spline's raw
                // parameter (0.5) when the composite parameter is halfway.
                Vector3 expected = expectedTangent(composite, parameter);
                Vector3 actual = composite.tangentAt(parameter);
                assertEquals(expected.x(), actual.x(), 1e-4, "x at " + parameter);
                assertEquals(expected.y(), actual.y(), 1e-4, "y at " + parameter);
                assertEquals(expected.z(), actual.z(), 1e-4, "z at " + parameter);
            }
        }
    }

    private static Vector3 expectedTangent(CompositeCurve3 composite, double parameter) {
        double eps = 1.0e-6;
        Vector3 tangent = composite.pointAt(parameter + eps).subtract(composite.pointAt(parameter - eps));
        return tangent.scale(1.0 / tangent.norm());
    }

    @Test
    void compositeCurveKeepsANestedTrimInsideItsSegment() {
        TrimmedCurve3 inner =
                new TrimmedCurve3(new Line3(new CartesianPoint(0, 0, 0), new Direction3(1, 0, 0)), 2.0, 8.0, true);
        TrimmedCurve3 outer = new TrimmedCurve3(inner, 0.25, 0.75, true);

        CompositeCurve3 composite = new CompositeCurve3(List.of(outer));

        assertEquals(5.0, composite.pointAt(0.5).x(), 1e-12);
        // The point sits half way along the outer segment, whose window is a
        // fraction of the inner curve. The innermost basis parameter is 5.0, so
        // a composite that reported that value would be far outside its segment.
        assertEquals(0.5, composite.parameterAt(new CartesianPoint(5.0, 0.0, 0.0)), 1e-9);
    }

    @Test
    void compositeCurveLengthRespectsLineScale() {
        Line3 scaledLine = new Line3(
            new CartesianPoint(0, 0, 0),
            new Direction3(1, 0, 0),
            2.0
        );

        CompositeCurve3 composite = new CompositeCurve3(List.of(scaledLine));

        assertEquals(2.0, composite.length(), 1e-10);
    }
}
