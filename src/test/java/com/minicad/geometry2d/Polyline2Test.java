package com.minicad.geometry2d;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for Polyline2 class.
 */
class Polyline2Test {

    @Test
    void polylineContains() {
        Polyline2 polyline = new Polyline2(List.of(
            new Point2(0, 0),
            new Point2(10, 0),
            new Point2(10, 10)
        ));

        assertTrue(polyline.contains(new Point2(5, 0)));
        assertTrue(polyline.contains(new Point2(10, 5)));
        assertFalse(polyline.contains(new Point2(5, 5)));
    }

    @Test
    void polylineSample() {
        Polyline2 polyline = new Polyline2(List.of(
            new Point2(0, 0),
            new Point2(10, 0),
            new Point2(10, 10)
        ));

        List<Point2> samples = polyline.sample();
        assertEquals(3, samples.size());
        assertEquals(new Point2(0, 0), samples.get(0));
        assertEquals(new Point2(10, 0), samples.get(1));
        assertEquals(new Point2(10, 10), samples.get(2));
    }

    @Test
    void polylineSampleWithInterpolation() {
        Polyline2 polyline = new Polyline2(List.of(
            new Point2(0, 0),
            new Point2(10, 0)
        ));

        List<Point2> samples = polyline.sample(2);
        assertEquals(3, samples.size());
        assertEquals(0.0, samples.get(0).x(), 1e-10);
        assertEquals(5.0, samples.get(1).x(), 1e-10);
        assertEquals(10.0, samples.get(2).x(), 1e-10);
    }

    @Test
    void polylinePointAt() {
        Polyline2 polyline = new Polyline2(List.of(
            new Point2(0, 0),
            new Point2(10, 0),
            new Point2(10, 10)
        ));

        Point2 p0 = polyline.pointAt(0);
        assertEquals(0.0, p0.x(), 1e-10);

        Point2 p1 = polyline.pointAt(1);
        assertEquals(10.0, p1.x(), 1e-10);
        assertEquals(10.0, p1.y(), 1e-10);

        // Total length = 20, half length = 10, at (10, 0)
        Point2 p05 = polyline.pointAt(0.5);
        assertEquals(10.0, p05.x(), 1e-10);
        assertEquals(0.0, p05.y(), 1e-10);
    }

    @Test
    void polylineBoundingBox() {
        Polyline2 polyline = new Polyline2(List.of(
            new Point2(0, 0),
            new Point2(10, 5),
            new Point2(15, 10)
        ));

        BoundingBox2 box = polyline.boundingBox();
        assertEquals(0.0, box.minX(), 1e-10);
        assertEquals(0.0, box.minY(), 1e-10);
        assertEquals(15.0, box.maxX(), 1e-10);
        assertEquals(10.0, box.maxY(), 1e-10);
    }

    @Test
    void polylineLength() {
        Polyline2 polyline = new Polyline2(List.of(
            new Point2(0, 0),
            new Point2(10, 0),
            new Point2(10, 10)
        ));

        assertEquals(20.0, polyline.length(), 1e-10);
    }

    @Test
    void polylineTangentAt() {
        Polyline2 polyline = new Polyline2(List.of(
            new Point2(0, 0),
            new Point2(10, 0),
            new Point2(10, 10)
        ));

        Vector2 t0 = polyline.tangentAt(0);
        assertEquals(1.0, t0.x(), 1e-10);
        assertEquals(0.0, t0.y(), 1e-10);

        Vector2 t1 = polyline.tangentAt(0.75);
        assertEquals(0.0, t1.x(), 1e-10);
        assertEquals(1.0, t1.y(), 1e-10);
    }

    @Test
    void polylineClosestPointTo() {
        Polyline2 polyline = new Polyline2(List.of(
            new Point2(0, 0),
            new Point2(10, 0),
            new Point2(10, 10)
        ));

        Point2 nearFirst = new Point2(5, 5);
        Point2 closest = polyline.closestPointTo(nearFirst);
        assertEquals(5.0, closest.x(), 1e-10);
        assertEquals(0.0, closest.y(), 1e-10);
    }

    @Test
    void polylineDistanceTo() {
        Polyline2 polyline = new Polyline2(List.of(
            new Point2(0, 0),
            new Point2(10, 0),
            new Point2(10, 10)
        ));

        Point2 point = new Point2(5, 5);
        assertEquals(5.0, polyline.distanceTo(point), 1e-10);
    }

    @Test
    void polylineMidpoint() {
        Polyline2 polyline = new Polyline2(List.of(
            new Point2(0, 0),
            new Point2(10, 0),
            new Point2(10, 10)
        ));

        Point2 midpoint = polyline.midpoint();
        assertEquals(10.0, midpoint.x(), 1e-10);
        assertEquals(0.0, midpoint.y(), 1e-10);
    }

    @Test
    void polylineMidpointUnequalSegments() {
        Polyline2 polyline = new Polyline2(List.of(
            new Point2(0, 0),
            new Point2(10, 0),
            new Point2(10, 30)
        ));

        Point2 midpoint = polyline.midpoint();
        assertEquals(10.0, midpoint.x(), 1e-10);
        assertEquals(10.0, midpoint.y(), 1e-10);
    }

    @Test
    void polylineSegmentCount() {
        Polyline2 polyline = new Polyline2(List.of(
            new Point2(0, 0),
            new Point2(10, 0),
            new Point2(10, 10)
        ));

        assertEquals(2, polyline.segmentCount());
    }

    @Test
    void polylineStartEndPoint() {
        Polyline2 polyline = new Polyline2(List.of(
            new Point2(0, 0),
            new Point2(10, 0),
            new Point2(10, 10)
        ));

        assertEquals(new Point2(0, 0), polyline.startPoint());
        assertEquals(new Point2(10, 10), polyline.endPoint());
    }

    @Test
    void polylinePointCount() {
        Polyline2 polyline = new Polyline2(List.of(
            new Point2(0, 0),
            new Point2(10, 0),
            new Point2(10, 10)
        ));

        assertEquals(3, polyline.pointCount());
    }

    @Test
    void polylineRequiresAtLeastTwoPoints() {
        assertThrows(IllegalArgumentException.class, () ->
            new Polyline2(List.of(new Point2(0, 0)))
        );
    }

    @Test
    void polylineClosestPointToVertex() {
        Polyline2 polyline = new Polyline2(List.of(
            new Point2(0, 0),
            new Point2(10, 0),
            new Point2(10, 10)
        ));

        Point2 atVertex = new Point2(10, 0);
        Point2 closest = polyline.closestPointTo(atVertex);
        assertEquals(10.0, closest.x(), 1e-10);
        assertEquals(0.0, closest.y(), 1e-10);
    }

    @Test
    void polylineClosed() {
        Polyline2 polyline = new Polyline2(List.of(
            new Point2(0, 0),
            new Point2(10, 0),
            new Point2(10, 10),
            new Point2(0, 10),
            new Point2(0, 0)
        ));

        assertEquals(polyline.startPoint(), polyline.endPoint());
        assertEquals(40.0, polyline.length(), 1e-10);
        assertEquals(4, polyline.segmentCount());
    }
}