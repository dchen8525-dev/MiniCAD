package com.minicad.geometry2d;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for BSplineCurve2 class.
 */
class BSplineCurve2Test {

    @Test
    void bsplinePointAt() {
        // Linear B-spline (degree 1)
        List<Point2> controlPoints = List.of(
            new Point2(0, 0),
            new Point2(10, 0)
        );
        List<Double> knots = List.of(0.0, 1.0);
        List<Integer> multiplicities = List.of(2, 2);

        BSplineCurve2 bspline = new BSplineCurve2(1, controlPoints, multiplicities, knots);

        Point2 p0 = bspline.pointAt(0);
        assertEquals(0.0, p0.x(), 1e-10);

        Point2 p1 = bspline.pointAt(1);
        assertEquals(10.0, p1.x(), 1e-10);

        Point2 p05 = bspline.pointAt(0.5);
        assertEquals(5.0, p05.x(), 1e-10);
    }

    @Test
    void bsplineQuadraticPointAt() {
        // Quadratic B-spline (degree 2)
        List<Point2> controlPoints = List.of(
            new Point2(0, 0),
            new Point2(5, 10),
            new Point2(10, 0)
        );
        List<Double> knots = List.of(0.0, 0.5, 1.0);
        List<Integer> multiplicities = List.of(3, 1, 3);

        BSplineCurve2 bspline = new BSplineCurve2(2, controlPoints, multiplicities, knots);

        Point2 p0 = bspline.pointAt(bspline.startParameter());
        assertEquals(0.0, p0.x(), 1e-10);

        Point2 p1 = bspline.pointAt(bspline.endParameter());
        assertEquals(10.0, p1.x(), 1e-10);
    }

    @Test
    void bsplineContains() {
        List<Point2> controlPoints = List.of(
            new Point2(0, 0),
            new Point2(10, 0)
        );
        List<Double> knots = List.of(0.0, 1.0);
        List<Integer> multiplicities = List.of(2, 2);

        BSplineCurve2 bspline = new BSplineCurve2(1, controlPoints, multiplicities, knots);

        assertTrue(bspline.contains(new Point2(5, 0)));
    }

    @Test
    void bsplineSample() {
        List<Point2> controlPoints = List.of(
            new Point2(0, 0),
            new Point2(10, 0)
        );
        List<Double> knots = List.of(0.0, 1.0);
        List<Integer> multiplicities = List.of(2, 2);

        BSplineCurve2 bspline = new BSplineCurve2(1, controlPoints, multiplicities, knots);

        List<Point2> samples = bspline.sample(4);
        // Minimum segments is 8, so we get 9 points
        assertEquals(9, samples.size());
        assertEquals(0.0, samples.get(0).x(), 1e-10);
        assertEquals(10.0, samples.get(8).x(), 1e-10);
    }

    @Test
    void bsplineTangentAt() {
        List<Point2> controlPoints = List.of(
            new Point2(0, 0),
            new Point2(10, 0)
        );
        List<Double> knots = List.of(0.0, 1.0);
        List<Integer> multiplicities = List.of(2, 2);

        BSplineCurve2 bspline = new BSplineCurve2(1, controlPoints, multiplicities, knots);

        Vector2 tangent = bspline.tangentAt(0.5);
        assertEquals(1.0, tangent.x(), 1e-10);
        assertEquals(0.0, tangent.y(), 1e-10);
    }

    @Test
    void bsplineBoundingBox() {
        List<Point2> controlPoints = List.of(
            new Point2(0, 0),
            new Point2(10, 5)
        );
        List<Double> knots = List.of(0.0, 1.0);
        List<Integer> multiplicities = List.of(2, 2);

        BSplineCurve2 bspline = new BSplineCurve2(1, controlPoints, multiplicities, knots);

        BoundingBox2 box = bspline.boundingBox();
        assertEquals(0.0, box.minX(), 1e-10);
        assertEquals(10.0, box.maxX(), 1e-10);
    }

    @Test
    void bsplineBoundingBoxWithSampling() {
        List<Point2> controlPoints = List.of(
            new Point2(0, 0),
            new Point2(10, 5)
        );
        List<Double> knots = List.of(0.0, 1.0);
        List<Integer> multiplicities = List.of(2, 2);

        BSplineCurve2 bspline = new BSplineCurve2(1, controlPoints, multiplicities, knots);

        BoundingBox2 box = bspline.boundingBox(16);
        assertNotNull(box);
    }

    @Test
    void bsplineExpandedKnots() {
        List<Point2> controlPoints = List.of(
            new Point2(0, 0),
            new Point2(10, 0)
        );
        List<Double> knots = List.of(0.0, 1.0);
        List<Integer> multiplicities = List.of(2, 2);

        BSplineCurve2 bspline = new BSplineCurve2(1, controlPoints, multiplicities, knots);

        List<Double> expanded = bspline.expandedKnots();
        assertEquals(4, expanded.size());
        assertEquals(0.0, expanded.get(0), 1e-10);
        assertEquals(0.0, expanded.get(1), 1e-10);
        assertEquals(1.0, expanded.get(2), 1e-10);
        assertEquals(1.0, expanded.get(3), 1e-10);
    }

    @Test
    void bsplineStartEndParameter() {
        List<Point2> controlPoints = List.of(
            new Point2(0, 0),
            new Point2(10, 0)
        );
        List<Double> knots = List.of(0.0, 1.0);
        List<Integer> multiplicities = List.of(2, 2);

        BSplineCurve2 bspline = new BSplineCurve2(1, controlPoints, multiplicities, knots);

        assertEquals(0.0, bspline.startParameter(), 1e-10);
        assertEquals(1.0, bspline.endParameter(), 1e-10);
    }

    @Test
    void bsplineLength() {
        List<Point2> controlPoints = List.of(
            new Point2(0, 0),
            new Point2(10, 0)
        );
        List<Double> knots = List.of(0.0, 1.0);
        List<Integer> multiplicities = List.of(2, 2);

        BSplineCurve2 bspline = new BSplineCurve2(1, controlPoints, multiplicities, knots);

        assertEquals(10.0, bspline.length(), 0.1);
    }

    @Test
    void bsplineClosestPointTo() {
        List<Point2> controlPoints = List.of(
            new Point2(0, 0),
            new Point2(10, 0)
        );
        List<Double> knots = List.of(0.0, 1.0);
        List<Integer> multiplicities = List.of(2, 2);

        BSplineCurve2 bspline = new BSplineCurve2(1, controlPoints, multiplicities, knots);

        Point2 point = new Point2(5, 5);
        Point2 closest = bspline.closestPointTo(point);
        assertEquals(5.0, closest.x(), 0.1);
        assertEquals(0.0, closest.y(), 0.1);
    }

    @Test
    void bsplineDistanceTo() {
        List<Point2> controlPoints = List.of(
            new Point2(0, 0),
            new Point2(10, 0)
        );
        List<Double> knots = List.of(0.0, 1.0);
        List<Integer> multiplicities = List.of(2, 2);

        BSplineCurve2 bspline = new BSplineCurve2(1, controlPoints, multiplicities, knots);

        Point2 point = new Point2(5, 5);
        assertEquals(5.0, bspline.distanceTo(point), 0.1);
    }

    @Test
    void bsplineMidpoint() {
        List<Point2> controlPoints = List.of(
            new Point2(0, 0),
            new Point2(10, 0)
        );
        List<Double> knots = List.of(0.0, 1.0);
        List<Integer> multiplicities = List.of(2, 2);

        BSplineCurve2 bspline = new BSplineCurve2(1, controlPoints, multiplicities, knots);

        Point2 midpoint = bspline.midpoint();
        assertEquals(5.0, midpoint.x(), 1e-10);
    }

    @Test
    void bsplineControlPointCount() {
        List<Point2> controlPoints = List.of(
            new Point2(0, 0),
            new Point2(10, 0)
        );
        List<Double> knots = List.of(0.0, 1.0);
        List<Integer> multiplicities = List.of(2, 2);

        BSplineCurve2 bspline = new BSplineCurve2(1, controlPoints, multiplicities, knots);

        assertEquals(2, bspline.controlPointCount());
    }

    @Test
    void bsplineKnotCount() {
        List<Point2> controlPoints = List.of(
            new Point2(0, 0),
            new Point2(10, 0)
        );
        List<Double> knots = List.of(0.0, 1.0);
        List<Integer> multiplicities = List.of(2, 2);

        BSplineCurve2 bspline = new BSplineCurve2(1, controlPoints, multiplicities, knots);

        assertEquals(2, bspline.knotCount());
    }

    @Test
    void bsplineValidationInvalidDegree() {
        List<Point2> controlPoints = List.of(
            new Point2(0, 0),
            new Point2(10, 0)
        );
        List<Double> knots = List.of(0.0, 1.0);
        List<Integer> multiplicities = List.of(2, 2);

        assertThrows(Exception.class, () -> new BSplineCurve2(0, controlPoints, multiplicities, knots));
    }

    @Test
    void bsplineValidationControlPointCount() {
        List<Point2> controlPoints = List.of(new Point2(0, 0));
        List<Double> knots = List.of(0.0, 1.0);
        List<Integer> multiplicities = List.of(2, 2);

        assertThrows(Exception.class, () -> new BSplineCurve2(2, controlPoints, multiplicities, knots));
    }

    @Test
    void bsplineCubic() {
        // Cubic B-spline (degree 3)
        List<Point2> controlPoints = List.of(
            new Point2(0, 0),
            new Point2(3, 10),
            new Point2(7, 10),
            new Point2(10, 0)
        );
        List<Double> knots = List.of(0.0, 1.0);
        List<Integer> multiplicities = List.of(4, 4);

        BSplineCurve2 bspline = new BSplineCurve2(3, controlPoints, multiplicities, knots);

        assertEquals(0.0, bspline.startParameter(), 1e-10);
        assertEquals(1.0, bspline.endParameter(), 1e-10);

        Point2 p0 = bspline.pointAt(0);
        assertEquals(0.0, p0.x(), 1e-10);

        Point2 p1 = bspline.pointAt(1);
        assertEquals(10.0, p1.x(), 1e-10);
    }
}