package com.minicad.geometry2d;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for Parabola2 class.
 */
class Parabola2Test {

    @Test
    void parabolaPointAt() {
        Point2 vertex = new Point2(0, 0);
        Direction2 axisDir = new Direction2(0, 1);  // opening upward
        Parabola2 parabola = new Parabola2(vertex, axisDir, 2.0);

        // Point at t=0 (vertex)
        Point2 p0 = parabola.pointAt(0);
        assertEquals(0.0, p0.x(), 1e-10);
        assertEquals(0.0, p0.y(), 1e-10);

        // Point at t=1: perpendicular = -4, axis = 2 (perpendicular direction is (-axis.y, axis.x) = (-1, 0))
        Point2 p1 = parabola.pointAt(1);
        assertEquals(-4.0, p1.x(), 1e-10);
        assertEquals(2.0, p1.y(), 1e-10);

        // Point at t=-1 (symmetric)
        Point2 pNeg1 = parabola.pointAt(-1);
        assertEquals(4.0, pNeg1.x(), 1e-10);
        assertEquals(2.0, pNeg1.y(), 1e-10);
    }

    @Test
    void parabolaContains() {
        Point2 vertex = new Point2(0, 0);
        Direction2 axisDir = new Direction2(0, 1);
        Parabola2 parabola = new Parabola2(vertex, axisDir, 2.0);

        // Point at vertex
        assertTrue(parabola.contains(new Point2(0, 0)));

        // Point at t=1: (-4, 2)
        assertTrue(parabola.contains(new Point2(-4, 2)));

        // Point not on parabola
        assertFalse(parabola.contains(new Point2(1, 1)));
    }

    @Test
    void parabolaSample() {
        Point2 vertex = new Point2(0, 0);
        Direction2 axisDir = new Direction2(0, 1);
        Parabola2 parabola = new Parabola2(vertex, axisDir, 2.0);

        java.util.List<Point2> samples = parabola.sample(4);
        assertEquals(5, samples.size());

        // First point at t=-1 (x = 4)
        assertEquals(4.0, samples.get(0).x(), 1e-10);

        // Last point at t=1 (x = -4)
        assertEquals(-4.0, samples.get(4).x(), 1e-10);
    }

    @Test
    void parabolaSampleWithRange() {
        Point2 vertex = new Point2(0, 0);
        Direction2 axisDir = new Direction2(0, 1);
        Parabola2 parabola = new Parabola2(vertex, axisDir, 2.0);

        java.util.List<Point2> samples = parabola.sample(4, 0.0, 1.0);
        assertEquals(5, samples.size());
        assertEquals(0.0, samples.get(0).x(), 1e-10);
        assertEquals(-4.0, samples.get(4).x(), 1e-10);
    }

    @Test
    void parabolaTangentAt() {
        Point2 vertex = new Point2(0, 0);
        Direction2 axisDir = new Direction2(0, 1);
        Parabola2 parabola = new Parabola2(vertex, axisDir, 2.0);

        // Tangent at vertex (t=0) is perpendicular to axis = (-1, 0)
        Vector2 t0 = parabola.tangentAt(0);
        assertEquals(-1.0, t0.x(), 1e-10);
        assertEquals(0.0, t0.y(), 1e-10);
    }

    @Test
    void parabolaNormalAt() {
        Point2 vertex = new Point2(0, 0);
        Direction2 axisDir = new Direction2(0, 1);
        Parabola2 parabola = new Parabola2(vertex, axisDir, 2.0);

        Vector2 n0 = parabola.normalAt(0);
        assertTrue(n0.norm() > 0.9 && n0.norm() < 1.1);
    }

    @Test
    void parabolaCurvatureAt() {
        Point2 vertex = new Point2(0, 0);
        Direction2 axisDir = new Direction2(0, 1);
        Parabola2 parabola = new Parabola2(vertex, axisDir, 2.0);

        // Curvature for parabola: k = 1/(2p) = 1/4 = 0.25
        double k = parabola.curvatureAt(0);
        assertEquals(0.25, k, 1e-10);
    }

    @Test
    void parabolaBoundingBox() {
        Point2 vertex = new Point2(0, 0);
        Direction2 axisDir = new Direction2(0, 1);
        Parabola2 parabola = new Parabola2(vertex, axisDir, 2.0);

        BoundingBox2 box = parabola.boundingBox();
        assertEquals(-4.0, box.minX(), 1e-10);
        assertEquals(4.0, box.maxX(), 1e-10);
        assertEquals(0.0, box.minY(), 1e-10);
        assertEquals(2.0, box.maxY(), 1e-10);
    }

    @Test
    void parabolaBoundingBoxWithRange() {
        Point2 vertex = new Point2(0, 0);
        Direction2 axisDir = new Direction2(0, 1);
        Parabola2 parabola = new Parabola2(vertex, axisDir, 2.0);

        BoundingBox2 box = parabola.boundingBox(0.0, 1.0);
        assertEquals(-4.0, box.minX(), 1e-10);
        assertEquals(0.0, box.maxX(), 1e-10);
    }

    @Test
    void parabolaClosestPointTo() {
        Point2 vertex = new Point2(0, 0);
        Direction2 axisDir = new Direction2(0, 1);
        Parabola2 parabola = new Parabola2(vertex, axisDir, 2.0);

        Point2 farRight = new Point2(10, 0);
        Point2 closest = parabola.closestPointTo(farRight);
        // Just verify the method returns a point
        assertNotNull(closest);
    }

    @Test
    void parabolaDistanceTo() {
        Point2 vertex = new Point2(0, 0);
        Direction2 axisDir = new Direction2(0, 1);
        Parabola2 parabola = new Parabola2(vertex, axisDir, 2.0);

        assertEquals(0.0, parabola.distanceTo(new Point2(0, 0)), 1e-10);
    }

    @Test
    void parabolaLength() {
        Point2 vertex = new Point2(0, 0);
        Direction2 axisDir = new Direction2(0, 1);
        Parabola2 parabola = new Parabola2(vertex, axisDir, 2.0);

        double length = parabola.length();
        assertTrue(length > 0);
    }

    @Test
    void parabolaFocus() {
        Point2 vertex = new Point2(0, 0);
        Direction2 axisDir = new Direction2(0, 1);
        Parabola2 parabola = new Parabola2(vertex, axisDir, 2.0);

        Point2 focus = parabola.focus();
        assertEquals(0.0, focus.x(), 1e-10);
        assertEquals(2.0, focus.y(), 1e-10);
    }

    @Test
    void parabolaDirectrix() {
        Point2 vertex = new Point2(0, 0);
        Direction2 axisDir = new Direction2(0, 1);
        Parabola2 parabola = new Parabola2(vertex, axisDir, 2.0);

        Line2 directrix = parabola.directrix();
        assertNotNull(directrix);
    }

    @Test
    void parabolaFocalLength() {
        Point2 vertex = new Point2(0, 0);
        Direction2 axisDir = new Direction2(0, 1);
        Parabola2 parabola = new Parabola2(vertex, axisDir, 2.0);

        assertEquals(2.0, parabola.focalLength(), 1e-10);
    }

    @Test
    void parabolaYDirection() {
        Point2 vertex = new Point2(0, 0);
        Direction2 axisDir = new Direction2(0, 1);
        Parabola2 parabola = new Parabola2(vertex, axisDir, 2.0);

        Direction2 yDir = parabola.yDirection();
        // perpendicular = (-axis.y, axis.x) = (-1, 0)
        assertEquals(-1.0, yDir.x(), 1e-10);
        assertEquals(0.0, yDir.y(), 1e-10);
    }

    @Test
    void parabolaStaticAt() {
        Parabola2 parabola = Parabola2.at(new Point2(10, 20), 2.0);

        assertEquals(new Point2(10, 20), parabola.vertex());
        assertEquals(2.0, parabola.focalDistance(), 1e-10);
    }

    @Test
    void parabolaValidation() {
        // Valid parabola
        new Parabola2(new Point2(0, 0), new Direction2(0, 1), 1.0);

        // Invalid - zero focal distance
        assertThrows(Exception.class, () -> new Parabola2(new Point2(0, 0), new Direction2(0, 1), 0.0));
    }

    @Test
    void parabolaOffsetVertex() {
        Point2 vertex = new Point2(10, 20);
        Direction2 axisDir = new Direction2(0, 1);
        Parabola2 parabola = new Parabola2(vertex, axisDir, 2.0);

        Point2 p = parabola.pointAt(1);
        // perpendicular offset = -4, axis offset = 2
        assertEquals(6.0, p.x(), 1e-10);  // 10 - 4
        assertEquals(22.0, p.y(), 1e-10);  // 20 + 2
    }
}