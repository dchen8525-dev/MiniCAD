package com.minicad.geometry;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for Polyline3 class.
 */
class Polyline3Test {

    @Test
    void polylineContains() {
        Polyline3 polyline = new Polyline3(List.of(
            new CartesianPoint(0, 0, 0),
            new CartesianPoint(10, 0, 0),
            new CartesianPoint(10, 10, 0)
        ));

        // Point on the polyline
        assertTrue(polyline.contains(new CartesianPoint(5, 0, 0)));
        assertTrue(polyline.contains(new CartesianPoint(10, 5, 0)));

        // Point not on the polyline
        assertFalse(polyline.contains(new CartesianPoint(5, 5, 0)));
    }

    @Test
    void polylineSample() {
        Polyline3 polyline = new Polyline3(List.of(
            new CartesianPoint(0, 0, 0),
            new CartesianPoint(10, 0, 0),
            new CartesianPoint(10, 10, 0)
        ));

        // Default sample returns all vertices
        List<CartesianPoint> samples = polyline.sample();
        assertEquals(3, samples.size());
        assertEquals(new CartesianPoint(0, 0, 0), samples.get(0));
        assertEquals(new CartesianPoint(10, 0, 0), samples.get(1));
        assertEquals(new CartesianPoint(10, 10, 0), samples.get(2));
    }

    @Test
    void polylineSampleWithInterpolation() {
        Polyline3 polyline = new Polyline3(List.of(
            new CartesianPoint(0, 0, 0),
            new CartesianPoint(10, 0, 0)
        ));

        // Sample with 2 segments per edge
        List<CartesianPoint> samples = polyline.sample(2);
        assertEquals(3, samples.size());  // 0, 5, 10
        assertEquals(0.0, samples.get(0).x(), 1e-10);
        assertEquals(5.0, samples.get(1).x(), 1e-10);
        assertEquals(10.0, samples.get(2).x(), 1e-10);
    }

    @Test
    void polylinePointAt() {
        Polyline3 polyline = new Polyline3(List.of(
            new CartesianPoint(0, 0, 0),
            new CartesianPoint(10, 0, 0),
            new CartesianPoint(10, 10, 0)
        ));

        // Point at parameter=0 should be start point
        CartesianPoint p0 = polyline.pointAt(0);
        assertEquals(0.0, p0.x(), 1e-10);
        assertEquals(0.0, p0.y(), 1e-10);

        // Point at parameter=1 should be end point
        CartesianPoint p1 = polyline.pointAt(1);
        assertEquals(10.0, p1.x(), 1e-10);
        assertEquals(10.0, p1.y(), 1e-10);

        // Point at parameter=0.5 should be midpoint of second segment
        // Total length = 20, half length = 10, at (10, 0, 0)
        CartesianPoint p05 = polyline.pointAt(0.5);
        assertEquals(10.0, p05.x(), 1e-10);
        assertEquals(0.0, p05.y(), 1e-10);
    }

    @Test
    void polylineBoundingBox() {
        Polyline3 polyline = new Polyline3(List.of(
            new CartesianPoint(0, 0, 0),
            new CartesianPoint(10, 5, 3),
            new CartesianPoint(15, 10, 2)
        ));

        BoundingBox3 box = polyline.boundingBox();
        assertEquals(0.0, box.minX(), 1e-10);
        assertEquals(0.0, box.minY(), 1e-10);
        assertEquals(0.0, box.minZ(), 1e-10);
        assertEquals(15.0, box.maxX(), 1e-10);
        assertEquals(10.0, box.maxY(), 1e-10);
        assertEquals(3.0, box.maxZ(), 1e-10);
    }

    @Test
    void polylineLength() {
        Polyline3 polyline = new Polyline3(List.of(
            new CartesianPoint(0, 0, 0),
            new CartesianPoint(10, 0, 0),
            new CartesianPoint(10, 10, 0)
        ));

        double length = polyline.length();
        assertEquals(20.0, length, 1e-10);  // 10 + 10
    }

    @Test
    void polylineTangentAt() {
        Polyline3 polyline = new Polyline3(List.of(
            new CartesianPoint(0, 0, 0),
            new CartesianPoint(10, 0, 0),
            new CartesianPoint(10, 10, 0)
        ));

        // Tangent along first segment (X direction)
        Vector3 t0 = polyline.tangentAt(0);
        assertEquals(1.0, t0.x(), 1e-10);
        assertEquals(0.0, t0.y(), 1e-10);

        // Tangent along second segment (Y direction)
        Vector3 t1 = polyline.tangentAt(0.75);
        assertEquals(0.0, t1.x(), 1e-10);
        assertEquals(1.0, t1.y(), 1e-10);
    }

    @Test
    void polylineClosestPointTo() {
        Polyline3 polyline = new Polyline3(List.of(
            new CartesianPoint(0, 0, 0),
            new CartesianPoint(10, 0, 0),
            new CartesianPoint(10, 10, 0)
        ));

        // Point near first segment
        CartesianPoint nearFirst = new CartesianPoint(5, 5, 0);
        CartesianPoint closestFirst = polyline.closestPointTo(nearFirst);
        assertEquals(5.0, closestFirst.x(), 1e-10);
        assertEquals(0.0, closestFirst.y(), 1e-10);

        // Point near second segment
        CartesianPoint nearSecond = new CartesianPoint(5, 5, 0);
        // Closest point is on first segment at (5, 0)
        assertEquals(5.0, polyline.closestPointTo(nearSecond).x(), 1e-10);
    }

    @Test
    void polylineDistanceTo() {
        Polyline3 polyline = new Polyline3(List.of(
            new CartesianPoint(0, 0, 0),
            new CartesianPoint(10, 0, 0),
            new CartesianPoint(10, 10, 0)
        ));

        // Point at (5, 5, 0) - distance to first segment is 5
        CartesianPoint point = new CartesianPoint(5, 5, 0);
        double distance = polyline.distanceTo(point);
        assertEquals(5.0, distance, 1e-10);
    }

    @Test
    void polylineSegmentCount() {
        Polyline3 polyline = new Polyline3(List.of(
            new CartesianPoint(0, 0, 0),
            new CartesianPoint(10, 0, 0),
            new CartesianPoint(10, 10, 0)
        ));

        assertEquals(2, polyline.segmentCount());
    }

    @Test
    void polylineStartEndPoint() {
        Polyline3 polyline = new Polyline3(List.of(
            new CartesianPoint(0, 0, 0),
            new CartesianPoint(10, 0, 0),
            new CartesianPoint(10, 10, 0)
        ));

        assertEquals(new CartesianPoint(0, 0, 0), polyline.startPoint());
        assertEquals(new CartesianPoint(10, 10, 0), polyline.endPoint());
    }

    @Test
    void polylineMidpoint() {
        Polyline3 polyline = new Polyline3(List.of(
            new CartesianPoint(0, 0, 0),
            new CartesianPoint(10, 0, 0),
            new CartesianPoint(10, 10, 0)
        ));

        // Total length 20, midpoint at length 10 = (10, 0, 0)
        CartesianPoint midpoint = polyline.midpoint();
        assertEquals(10.0, midpoint.x(), 1e-10);
        assertEquals(0.0, midpoint.y(), 1e-10);
    }

    @Test
    void polylineMidpointUnequalSegments() {
        Polyline3 polyline = new Polyline3(List.of(
            new CartesianPoint(0, 0, 0),
            new CartesianPoint(10, 0, 0),
            new CartesianPoint(10, 30, 0)
        ));

        // Total length 40, midpoint at length 20 = (10, 10, 0)
        CartesianPoint midpoint = polyline.midpoint();
        assertEquals(10.0, midpoint.x(), 1e-10);
        assertEquals(10.0, midpoint.y(), 1e-10);
    }

    @Test
    void polylineRequiresAtLeastTwoPoints() {
        assertThrows(IllegalArgumentException.class, () ->
            new Polyline3(List.of(new CartesianPoint(0, 0, 0)))
        );
    }

    @Test
    void polylineClosestPointToVertex() {
        Polyline3 polyline = new Polyline3(List.of(
            new CartesianPoint(0, 0, 0),
            new CartesianPoint(10, 0, 0),
            new CartesianPoint(10, 10, 0)
        ));

        // Point exactly at a vertex
        CartesianPoint atVertex = new CartesianPoint(10, 0, 0);
        CartesianPoint closest = polyline.closestPointTo(atVertex);
        assertEquals(10.0, closest.x(), 1e-10);
        assertEquals(0.0, closest.y(), 1e-10);
    }

    @Test
    void polylineClosed() {
        Polyline3 polyline = new Polyline3(List.of(
            new CartesianPoint(0, 0, 0),
            new CartesianPoint(10, 0, 0),
            new CartesianPoint(10, 10, 0),
            new CartesianPoint(0, 10, 0),
            new CartesianPoint(0, 0, 0)
        ));

        // Closed polyline - start and end are same
        assertEquals(polyline.startPoint(), polyline.endPoint());

        // Total length = 10 + 10 + 10 + 10 = 40
        assertEquals(40.0, polyline.length(), 1e-10);

        // 4 segments (even though 5 points)
        assertEquals(4, polyline.segmentCount());
    }
}