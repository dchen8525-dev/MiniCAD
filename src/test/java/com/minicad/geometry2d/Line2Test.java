package com.minicad.geometry2d;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for Line2 utility methods.
 */
class Line2Test {

    @Test
    void linePointAt() {
        Point2 origin = new Point2(0, 0);
        Direction2 direction = new Direction2(1, 0);
        Line2 line = new Line2(origin, direction);

        Point2 p0 = line.pointAt(0);
        assertEquals(0.0, p0.x(), 1e-10);
        assertEquals(0.0, p0.y(), 1e-10);

        Point2 p5 = line.pointAt(5);
        assertEquals(5.0, p5.x(), 1e-10);
        assertEquals(0.0, p5.y(), 1e-10);

        Point2 pNeg5 = line.pointAt(-5);
        assertEquals(-5.0, pNeg5.x(), 1e-10);
        assertEquals(0.0, pNeg5.y(), 1e-10);
    }

    @Test
    void lineParameterOf() {
        Point2 origin = new Point2(0, 0);
        Direction2 direction = new Direction2(1, 0);
        Line2 line = new Line2(origin, direction);

        Point2 p5 = new Point2(5, 0);
        assertEquals(5.0, line.parameterOf(p5), 1e-10);

        Point2 pNeg5 = new Point2(-5, 0);
        assertEquals(-5.0, line.parameterOf(pNeg5), 1e-10);
    }

    @Test
    void lineClosestPoint() {
        Point2 origin = new Point2(0, 0);
        Direction2 direction = new Direction2(1, 0);
        Line2 line = new Line2(origin, direction);

        Point2 onLine = new Point2(5, 0);
        Point2 closestOn = line.closestPoint(onLine);
        assertEquals(5.0, closestOn.x(), 1e-10);
        assertEquals(0.0, closestOn.y(), 1e-10);

        Point2 offLine = new Point2(5, 10);
        Point2 closestOff = line.closestPoint(offLine);
        assertEquals(5.0, closestOff.x(), 1e-10);
        assertEquals(0.0, closestOff.y(), 1e-10);
    }

    @Test
    void lineContains() {
        Point2 origin = new Point2(0, 0);
        Direction2 direction = new Direction2(1, 0);
        Line2 line = new Line2(origin, direction);

        assertTrue(line.contains(new Point2(5, 0)));
        assertTrue(line.contains(new Point2(-10, 0)));
        assertFalse(line.contains(new Point2(5, 1)));
    }

    @Test
    void lineSample() {
        Point2 origin = new Point2(0, 0);
        Direction2 direction = new Direction2(1, 0);
        Line2 line = new Line2(origin, direction);

        java.util.List<Point2> samples = line.sample(5, 0, 10);
        assertEquals(6, samples.size());
        assertEquals(0.0, samples.get(0).x(), 1e-10);
        assertEquals(10.0, samples.get(5).x(), 1e-10);
    }

    @Test
    void lineRespectsParameterScale() {
        Line2 line = new Line2(new Point2(0, 0), new Direction2(1, 0), Math.PI);

        assertEquals(Math.PI, line.pointAt(1.0).x(), 1e-10);
        assertEquals(1.0, line.parameterOf(new Point2(Math.PI, 0)), 1e-10);
        assertEquals(Math.PI, line.length(), 1e-10);
    }

    @Test
    void lineTangentAt() {
        Point2 origin = new Point2(0, 0);
        Direction2 direction = new Direction2(1, 0);
        Line2 line = new Line2(origin, direction);

        Vector2 tangent = line.tangentAt(0);
        assertEquals(1.0, tangent.x(), 1e-10);
        assertEquals(0.0, tangent.y(), 1e-10);

        // Tangent is constant for all parameters
        Vector2 tangent5 = line.tangentAt(5);
        assertEquals(tangent.x(), tangent5.x(), 1e-10);
        assertEquals(tangent.y(), tangent5.y(), 1e-10);
    }

    @Test
    void lineNormalAt() {
        Point2 origin = new Point2(0, 0);
        Direction2 direction = new Direction2(1, 0);
        Line2 line = new Line2(origin, direction);

        Vector2 normal = line.normalAt(0);
        assertEquals(0.0, normal.x(), 1e-10);
        assertEquals(1.0, Math.abs(normal.y()), 1e-10);

        // Normal is perpendicular to direction
        assertEquals(0.0, normal.dot(direction.asVector()), 1e-10);
    }

    @Test
    void lineCurvature() {
        Point2 origin = new Point2(0, 0);
        Direction2 direction = new Direction2(1, 0);
        Line2 line = new Line2(origin, direction);

        assertEquals(0.0, line.curvature(), 1e-10);
        assertEquals(0.0, line.curvatureAt(0), 1e-10);
        assertEquals(0.0, line.curvatureAt(10), 1e-10);
    }

    @Test
    void lineDistanceTo() {
        Point2 origin = new Point2(0, 0);
        Direction2 direction = new Direction2(1, 0);
        Line2 line = new Line2(origin, direction);

        // Point on the line
        assertEquals(0.0, line.distanceTo(new Point2(5, 0)), 1e-10);

        // Point off the line
        assertEquals(5.0, line.distanceTo(new Point2(5, 5)), 1e-10);
    }

    @Test
    void lineSignedDistanceTo() {
        Point2 origin = new Point2(0, 0);
        Direction2 direction = new Direction2(1, 0);
        Line2 line = new Line2(origin, direction);

        // Signed distance uses cross product: offset.cross(direction)
        // For point (5, 5), offset = (5, 5), cross with (1, 0) = 5*0 - 5*1 = -5
        double distAbove = line.signedDistanceTo(new Point2(5, 5));
        assertEquals(-5.0, distAbove, 1e-10);

        // For point (5, -5), offset = (5, -5), cross with (1, 0) = 5*0 - (-5)*1 = 5
        double distBelow = line.signedDistanceTo(new Point2(5, -5));
        assertEquals(5.0, distBelow, 1e-10);
    }

    @Test
    void lineProject() {
        Point2 origin = new Point2(0, 0);
        Direction2 direction = new Direction2(1, 0);
        Line2 line = new Line2(origin, direction);

        Point2 offLine = new Point2(5, 10);
        Point2 projected = line.project(offLine);
        assertEquals(5.0, projected.x(), 1e-10);
        assertEquals(0.0, projected.y(), 1e-10);
    }

    @Test
    void lineParallelThrough() {
        Point2 origin = new Point2(0, 0);
        Direction2 direction = new Direction2(1, 0);
        Line2 line = new Line2(origin, direction);

        Point2 through = new Point2(0, 10);
        Line2 parallel = line.parallelThrough(through);

        assertTrue(parallel.isParallelTo(line));
        assertEquals(through, parallel.origin());
        assertEquals(direction, parallel.direction());
    }

    @Test
    void linePerpendicularThrough() {
        Point2 origin = new Point2(0, 0);
        Direction2 direction = new Direction2(1, 0);
        Line2 line = new Line2(origin, direction);

        Point2 through = new Point2(5, 5);
        Line2 perpendicular = line.perpendicularThrough(through);

        // Perpendicular direction is (0, 1) or (0, -1)
        assertEquals(0.0, Math.abs(perpendicular.direction().x()), 1e-10);
        assertEquals(1.0, Math.abs(perpendicular.direction().y()), 1e-10);
        assertEquals(through, perpendicular.origin());
    }

    @Test
    void lineIntersect() {
        Line2 line1 = new Line2(new Point2(0, 0), new Direction2(1, 0));
        Line2 line2 = new Line2(new Point2(0, 0), new Direction2(0, 1));

        Point2 intersection = line1.intersect(line2);
        assertNotNull(intersection);
        assertEquals(0.0, intersection.x(), 1e-10);
        assertEquals(0.0, intersection.y(), 1e-10);
    }

    @Test
    void lineIntersectOffset() {
        // Horizontal line at y=0
        Line2 line1 = new Line2(new Point2(0, 0), new Direction2(1, 0));
        // Vertical line at x=5 (Y-axis shifted by 5 units)
        Line2 line2 = new Line2(new Point2(5, 0), new Direction2(0, 1));

        Point2 intersection = line1.intersect(line2);
        assertNotNull(intersection);
        assertEquals(5.0, intersection.x(), 1e-10);
        assertEquals(0.0, intersection.y(), 1e-10);
    }

    @Test
    void lineIntersectParallelReturnsNull() {
        Line2 line1 = new Line2(new Point2(0, 0), new Direction2(1, 0));
        Line2 line2 = new Line2(new Point2(0, 5), new Direction2(1, 0));

        Point2 intersection = line1.intersect(line2);
        assertNull(intersection);
    }

    @Test
    void lineIsParallelTo() {
        Line2 line1 = new Line2(new Point2(0, 0), new Direction2(1, 0));
        Line2 line2 = new Line2(new Point2(0, 5), new Direction2(1, 0));

        assertTrue(line1.isParallelTo(line2));

        Line2 line3 = new Line2(new Point2(0, 0), new Direction2(0, 1));
        assertFalse(line1.isParallelTo(line3));
    }

    @Test
    void lineIsCoincidentWith() {
        Line2 line1 = new Line2(new Point2(0, 0), new Direction2(1, 0));
        Line2 line2 = new Line2(new Point2(5, 0), new Direction2(1, 0));

        assertTrue(line1.isCoincidentWith(line2));

        Line2 line3 = new Line2(new Point2(0, 5), new Direction2(1, 0));
        assertFalse(line1.isCoincidentWith(line3));
    }
}
