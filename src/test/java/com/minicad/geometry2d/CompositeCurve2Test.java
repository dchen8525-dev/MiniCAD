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
        assertFalse(composite.contains(new Point2(5, 5)));
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
            0.0,
            10.0,
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

    @Test
    void compositeCurveLengthRespectsLineScale() {
        CompositeCurve2 composite = new CompositeCurve2(List.of(
                new Line2(new Point2(0, 0), new Direction2(1, 0), 2.5)
        ));

        assertEquals(2.5, composite.length(), 1e-10);
    }

    @Test
    void compositeCurveMapsNonTrimmedSegmentByPosition() {
        CompositeCurve2 composite = new CompositeCurve2(List.of(
                new Line2(new Point2(0, 0), new Direction2(1, 0)),
                new Line2(new Point2(1, 0), new Direction2(1, 0))
        ));

        assertEquals(0.25, composite.parameterOf(new Point2(0.5, 0)), 1e-3);
        assertEquals(0.75, composite.parameterOf(new Point2(1.5, 0)), 1e-3);
    }

    @Test
    void compositeCurveFallsBackToTheNearestSampleOffEverySegment() {
        CompositeCurve2 composite = new CompositeCurve2(List.of(
                new Line2(new Point2(0, 0), new Direction2(1, 0))
        ));

        assertEquals(0.4, composite.parameterOf(new Point2(0.4, 5.0)), 1e-3);
    }

    @Test
    void compositeCurveMapsNormalizedParameterIntoBsplineDomain() {
        BSplineCurve2 bspline = new BSplineCurve2(
                1,
                List.of(new Point2(0, 0), new Point2(1, 0), new Point2(2, 0)),
                List.of(2, 1, 2),
                List.of(0.0, 1.0, 2.0));

        CompositeCurve2 composite = new CompositeCurve2(List.of(bspline));

        assertEquals(1.0, composite.pointAt(0.5).x(), 1e-10);
        assertEquals(0.5, composite.parameterOf(new Point2(1.0, 0.0)), 1e-10);
    }

    @Test
    void compositeCurveMapsNormalizedParameterIntoRationalBsplineDomain() {
        RationalBSplineCurve2 rational = new RationalBSplineCurve2(
                1,
                List.of(new Point2(0, 0), new Point2(1, 0), new Point2(2, 0)),
                List.of(1.0, 1.0, 1.0),
                List.of(2, 1, 2),
                List.of(0.0, 1.0, 2.0));

        CompositeCurve2 composite = new CompositeCurve2(List.of(rational));

        // The rational twin has the same [0, 2] knot domain as the plain spline,
        // so the composite's 0.5 is the spline's 1.0, not its 0.5.
        assertEquals(1.0, composite.pointAt(0.5).x(), 1e-10);
        assertEquals(0.5, composite.parameterOf(new Point2(1.0, 0.0)), 1e-3);
    }

    @Test
    void compositeCurveKeepsANestedTrimInsideItsSegment() {
        TrimmedCurve2 inner = new TrimmedCurve2(
                new Line2(new Point2(0, 0), new Direction2(1, 0)), 2.0, 8.0, true);
        TrimmedCurve2 outer = new TrimmedCurve2(inner, 0.25, 0.75, true);
        CompositeCurve2 composite = new CompositeCurve2(List.of(outer));

        assertEquals(5.0, composite.pointAt(0.5).x(), 1e-12);
        // The outer window is a fraction of the inner curve, so 5.0 is half way
        // along the outer segment. Reading the innermost basis parameter (5.0)
        // as the outer window's parameter used to return 9.5 here.
        assertEquals(0.5, composite.parameterOf(new Point2(5.0, 0.0)), 1e-9);
    }

    @Test
    void compositeCurveMapsBsplineDomainInsideAMultiSegmentCurve() {
        BSplineCurve2 bspline = new BSplineCurve2(
                1,
                List.of(new Point2(0, 0), new Point2(1, 0), new Point2(2, 0)),
                List.of(2, 1, 2),
                List.of(0.0, 1.0, 2.0));
        CompositeCurve2 composite = new CompositeCurve2(List.of(
                bspline,
                new Line2(new Point2(10, 0), new Direction2(1, 0))));

        // The first half of the composite is the spline's [0, 2] knot range.
        assertEquals(1.0, composite.pointAt(0.25).x(), 1e-10);
        assertEquals(0.25, composite.parameterOf(new Point2(1.0, 0.0)), 0.01);
    }

    @Test
    void compositeCurveValueContract() {
        List<Curve2> segments = List.of(
                new Line2(new Point2(0, 0), new Direction2(1, 0)),
                new Line2(new Point2(1, 0), new Direction2(1, 0)));
        CompositeCurve2 curve = new CompositeCurve2(segments);

        assertTrue(curve.equals(curve));
        assertFalse(curve.equals(null));
        assertFalse(curve.equals("not a composite curve"));
        assertEquals(curve, new CompositeCurve2(segments));
        assertNotEquals(curve, new CompositeCurve2(List.of(
                new Line2(new Point2(0, 0), new Direction2(1, 0)))));
        assertEquals(curve.hashCode(), new CompositeCurve2(segments).hashCode());
        assertTrue(curve.toString().startsWith("CompositeCurve2{"));
        assertTrue(curve.toString().contains("segments="));
    }

    @Test
    void compositeCurveMapsNormalizedParameterIntoTrimmedCurve() {
        TrimmedCurve2 trimmed = new TrimmedCurve2(
                new Line2(new Point2(0, 0), new Direction2(1, 0)),
                2.0,
                6.0,
                false
        );
        CompositeCurve2 composite = new CompositeCurve2(List.of(trimmed));

        assertEquals(6.0, composite.pointAt(0.0).x(), 1e-10);
        assertEquals(4.0, composite.pointAt(0.5).x(), 1e-10);
        assertEquals(2.0, composite.pointAt(1.0).x(), 1e-10);
        assertEquals(0.0, composite.parameterOf(new Point2(6.0, 0.0)), 1e-10);
        assertEquals(0.5, composite.parameterOf(new Point2(4.0, 0.0)), 1e-10);
        assertEquals(1.0, composite.parameterOf(new Point2(2.0, 0.0)), 1e-10);
    }
}
