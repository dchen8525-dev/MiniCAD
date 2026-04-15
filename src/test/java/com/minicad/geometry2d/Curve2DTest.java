package com.minicad.geometry2d;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Tests for 2D curve tangent and curvature computations.
 */
class Curve2DTest {

    @Test
    void circle2TangentAt() {
        Point2 center = new Point2(0, 0);
        Direction2 xDir = new Direction2(1, 0);
        Circle2 circle = new Circle2(center, xDir, 2.0);

        // Tangent at angle=0 should point along +Y
        Vector2 t0 = circle.tangentAt(0);
        assertEquals(0.0, t0.x(), 1e-10);
        assertEquals(1.0, t0.y(), 1e-10);

        // Tangent at angle=PI/2 should point along -X
        Vector2 t90 = circle.tangentAt(Math.PI / 2);
        assertEquals(-1.0, t90.x(), 1e-10);
        assertEquals(0.0, t90.y(), 1e-10);
    }

    @Test
    void circle2NormalAt() {
        Point2 center = new Point2(0, 0);
        Direction2 xDir = new Direction2(1, 0);
        Circle2 circle = new Circle2(center, xDir, 2.0);

        // Normal at angle=0 should point along +X
        Vector2 n0 = circle.normalAt(0);
        assertEquals(1.0, n0.x(), 1e-10);
        assertEquals(0.0, n0.y(), 1e-10);

        // Normal at angle=PI/2 should point along +Y
        Vector2 n90 = circle.normalAt(Math.PI / 2);
        assertEquals(0.0, n90.x(), 1e-10);
        assertEquals(1.0, n90.y(), 1e-10);
    }

    @Test
    void circle2Curvature() {
        Point2 center = new Point2(0, 0);
        Direction2 xDir = new Direction2(1, 0);
        Circle2 circle = new Circle2(center, xDir, 2.0);

        // Curvature should be 1/radius
        assertEquals(0.5, circle.curvature(), 1e-10);
        assertEquals(0.5, circle.curvatureAt(0), 1e-10);
        assertEquals(0.5, circle.curvatureAt(Math.PI), 1e-10);
    }

    @Test
    void ellipse2TangentAt() {
        Point2 center = new Point2(0, 0);
        Direction2 xDir = new Direction2(1, 0);
        Ellipse2 ellipse = new Ellipse2(center, xDir, 4.0, 2.0);

        // Tangent at angle=0 should point along +Y
        Vector2 t0 = ellipse.tangentAt(0);
        assertEquals(0.0, t0.x(), 1e-10);
        assertEquals(1.0, t0.y(), 1e-10);

        // Tangent at angle=PI/2 should point along -X
        Vector2 t90 = ellipse.tangentAt(Math.PI / 2);
        assertEquals(-1.0, t90.x(), 1e-10);
        assertEquals(0.0, t90.y(), 1e-10);
    }

    @Test
    void ellipse2NormalAt() {
        Point2 center = new Point2(0, 0);
        Direction2 xDir = new Direction2(1, 0);
        Ellipse2 ellipse = new Ellipse2(center, xDir, 4.0, 2.0);

        // Normal at angle=0 should point along +X
        Vector2 n0 = ellipse.normalAt(0);
        assertEquals(1.0, n0.x(), 1e-10);
        assertEquals(0.0, n0.y(), 1e-10);

        // Normal at angle=PI/2 should point along +Y
        Vector2 n90 = ellipse.normalAt(Math.PI / 2);
        assertEquals(0.0, n90.x(), 1e-10);
        assertEquals(1.0, n90.y(), 1e-10);
    }

    @Test
    void ellipse2CurvatureAt() {
        Point2 center = new Point2(0, 0);
        Direction2 xDir = new Direction2(1, 0);
        Ellipse2 ellipse = new Ellipse2(center, xDir, 4.0, 2.0);

        // Curvature at angle=0 (at major axis endpoint): k = (a*b) / (b^2)^1.5 = a/b^2 = 4/4 = 1
        assertEquals(1.0, ellipse.curvatureAt(0), 1e-10);

        // Curvature at angle=PI/2 (at minor axis endpoint): k = (a*b) / (a^2)^1.5 = b/a^2 = 2/16 = 0.125
        assertEquals(0.125, ellipse.curvatureAt(Math.PI / 2), 1e-10);
    }

    @Test
    void line2TangentAt() {
        Point2 origin = new Point2(0, 0);
        Direction2 direction = Direction2.from(new Vector2(1, 2));
        Line2 line = new Line2(origin, direction);

        // Tangent should be constant for all parameters
        Vector2 t0 = line.tangentAt(0);
        Vector2 t1 = line.tangentAt(100);
        Vector2 t2 = line.tangentAt(-50);

        assertEquals(t0.x(), t1.x(), 1e-10);
        assertEquals(t0.y(), t1.y(), 1e-10);
        assertEquals(t0.x(), t2.x(), 1e-10);
        assertEquals(1.0, t0.norm(), 1e-10);
    }

    @Test
    void line2NormalAt() {
        Point2 origin = new Point2(0, 0);
        Direction2 direction = Direction2.from(new Vector2(1, 0));
        Line2 line = new Line2(origin, direction);

        // Normal should be perpendicular to direction
        Vector2 n = line.normalAt(0);
        assertEquals(0.0, n.x(), 1e-10);
        assertEquals(1.0, Math.abs(n.y()), 1e-10);
    }

    @Test
    void line2Curvature() {
        Point2 origin = new Point2(0, 0);
        Direction2 direction = Direction2.from(new Vector2(1, 0));
        Line2 line = new Line2(origin, direction);

        // Lines have zero curvature
        assertEquals(0.0, line.curvature(), 1e-10);
        assertEquals(0.0, line.curvatureAt(0), 1e-10);
        assertEquals(0.0, line.curvatureAt(100), 1e-10);
    }

    @Test
    void circle2ClosestPointTo() {
        Point2 center = new Point2(0, 0);
        Direction2 xDir = new Direction2(1, 0);
        Circle2 circle = new Circle2(center, xDir, 5.0);

        // Point outside circle
        Point2 outside = new Point2(10, 0);
        Point2 closest = circle.closestPointTo(outside);
        assertEquals(5.0, closest.x(), 0.1);
        assertEquals(0.0, closest.y(), 0.1);
    }

    @Test
    void circle2DistanceTo() {
        Point2 center = new Point2(0, 0);
        Direction2 xDir = new Direction2(1, 0);
        Circle2 circle = new Circle2(center, xDir, 5.0);

        Point2 outside = new Point2(10, 0);
        double distance = circle.distanceTo(outside);
        assertEquals(5.0, distance, 0.1);
    }

    @Test
    void ellipse2ClosestPointTo() {
        Point2 center = new Point2(0, 0);
        Direction2 xDir = new Direction2(1, 0);
        Ellipse2 ellipse = new Ellipse2(center, xDir, 4.0, 2.0);

        Point2 point = new Point2(10, 0);
        Point2 closest = ellipse.closestPointTo(point);
        assertNotNull(closest);
        assertTrue(ellipse.boundingBox().contains(closest));
    }

    @Test
    void ellipse2DistanceTo() {
        Point2 center = new Point2(0, 0);
        Direction2 xDir = new Direction2(1, 0);
        Ellipse2 ellipse = new Ellipse2(center, xDir, 4.0, 2.0);

        Point2 point = new Point2(10, 0);
        double distance = ellipse.distanceTo(point);
        assertTrue(distance >= 0);
    }

    @Test
    void hyperbola2ClosestPointTo() {
        Point2 center = new Point2(0, 0);
        Direction2 xDir = new Direction2(1, 0);
        Hyperbola2 hyperbola = new Hyperbola2(center, xDir, 5.0, 3.0);

        Point2 point = new Point2(20, 0);
        Point2 closest = hyperbola.closestPointTo(point);
        assertNotNull(closest);
    }

    @Test
    void hyperbola2DistanceTo() {
        Point2 center = new Point2(0, 0);
        Direction2 xDir = new Direction2(1, 0);
        Hyperbola2 hyperbola = new Hyperbola2(center, xDir, 5.0, 3.0);

        Point2 point = new Point2(20, 0);
        double distance = hyperbola.distanceTo(point);
        assertTrue(distance >= 0);
    }

    @Test
    void parabola2ClosestPointTo() {
        Point2 center = new Point2(0, 0);
        Direction2 xDir = new Direction2(1, 0);
        Parabola2 parabola = new Parabola2(center, xDir, 2.0);

        Point2 point = new Point2(10, 0);
        Point2 closest = parabola.closestPointTo(point);
        assertNotNull(closest);
    }

    @Test
    void parabola2DistanceTo() {
        Point2 center = new Point2(0, 0);
        Direction2 xDir = new Direction2(1, 0);
        Parabola2 parabola = new Parabola2(center, xDir, 2.0);

        Point2 point = new Point2(10, 0);
        double distance = parabola.distanceTo(point);
        assertTrue(distance >= 0);
    }

    @Test
    void polyline2ClosestPointTo() {
        java.util.List<Point2> points = java.util.List.of(
            new Point2(0, 0),
            new Point2(10, 0),
            new Point2(10, 10)
        );
        Polyline2 polyline = new Polyline2(points);

        // Point near the first segment
        Point2 nearFirst = new Point2(5, 5);
        Point2 closest = polyline.closestPointTo(nearFirst);
        assertEquals(5.0, closest.x(), 0.1);
        assertEquals(0.0, closest.y(), 0.1);
    }

    @Test
    void polyline2DistanceTo() {
        java.util.List<Point2> points = java.util.List.of(
            new Point2(0, 0),
            new Point2(10, 0),
            new Point2(10, 10)
        );
        Polyline2 polyline = new Polyline2(points);

        Point2 nearFirst = new Point2(5, 5);
        double distance = polyline.distanceTo(nearFirst);
        assertEquals(5.0, distance, 0.1);
    }
}