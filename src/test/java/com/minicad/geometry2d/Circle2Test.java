package com.minicad.geometry2d;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for Circle2 utility methods.
 */
class Circle2Test {

    @Test
    void circlePointAt() {
        Point2 center = new Point2(0, 0);
        Direction2 xDir = new Direction2(1, 0);
        Circle2 circle = new Circle2(center, xDir, 5.0);

        // Point at angle=0 should be at (radius, 0)
        Point2 p0 = circle.pointAt(0);
        assertEquals(5.0, p0.x(), 1e-10);
        assertEquals(0.0, p0.y(), 1e-10);

        // Point at angle=PI/2 should be at (0, radius)
        Point2 p90 = circle.pointAt(Math.PI / 2);
        assertEquals(0.0, p90.x(), 1e-10);
        assertEquals(5.0, p90.y(), 1e-10);

        // Point at angle=PI should be at (-radius, 0)
        Point2 p180 = circle.pointAt(Math.PI);
        assertEquals(-5.0, p180.x(), 1e-10);
        assertEquals(0.0, p180.y(), 1e-10);
    }

    @Test
    void circleContains() {
        Point2 center = new Point2(0, 0);
        Direction2 xDir = new Direction2(1, 0);
        Circle2 circle = new Circle2(center, xDir, 5.0);

        assertTrue(circle.contains(new Point2(5, 0)));
        assertTrue(circle.contains(new Point2(0, 5)));
        assertFalse(circle.contains(new Point2(3, 3)));
    }

    @Test
    void circleSample() {
        Point2 center = new Point2(0, 0);
        Direction2 xDir = new Direction2(1, 0);
        Circle2 circle = new Circle2(center, xDir, 5.0);

        java.util.List<Point2> samples = circle.sample(4);
        assertEquals(5, samples.size());  // 4 segments = 5 points
    }

    @Test
    void circleArcSample() {
        Point2 center = new Point2(0, 0);
        Direction2 xDir = new Direction2(1, 0);
        Circle2 circle = new Circle2(center, xDir, 5.0);

        java.util.List<Point2> samples = circle.sample(4, 0, Math.PI / 2);
        assertEquals(5, samples.size());
        assertEquals(5.0, samples.get(0).x(), 1e-10);
        assertEquals(0.0, samples.get(0).y(), 1e-10);
        assertEquals(0.0, samples.get(4).x(), 1e-10);
        assertEquals(5.0, samples.get(4).y(), 1e-10);
    }

    @Test
    void circleCircumference() {
        Point2 center = new Point2(0, 0);
        Direction2 xDir = new Direction2(1, 0);
        Circle2 circle = new Circle2(center, xDir, 5.0);

        assertEquals(2 * Math.PI * 5, circle.circumference(), 1e-10);
    }

    @Test
    void circleArea() {
        Point2 center = new Point2(0, 0);
        Direction2 xDir = new Direction2(1, 0);
        Circle2 circle = new Circle2(center, xDir, 5.0);

        assertEquals(Math.PI * 25, circle.area(), 1e-10);
    }

    @Test
    void circleArcLength() {
        Point2 center = new Point2(0, 0);
        Direction2 xDir = new Direction2(1, 0);
        Circle2 circle = new Circle2(center, xDir, 5.0);

        double arcLength = circle.arcLength(0, Math.PI / 2);
        assertEquals(5.0 * Math.PI / 2, arcLength, 1e-10);
    }

    @Test
    void circleBoundingBox() {
        Point2 center = new Point2(0, 0);
        Direction2 xDir = new Direction2(1, 0);
        Circle2 circle = new Circle2(center, xDir, 5.0);

        BoundingBox2 box = circle.boundingBox();
        assertEquals(-5.0, box.minX(), 1e-10);
        assertEquals(-5.0, box.minY(), 1e-10);
        assertEquals(5.0, box.maxX(), 1e-10);
        assertEquals(5.0, box.maxY(), 1e-10);
    }

    @Test
    void circleBoundingBoxOffset() {
        Point2 center = new Point2(10, 20);
        Direction2 xDir = new Direction2(1, 0);
        Circle2 circle = new Circle2(center, xDir, 5.0);

        BoundingBox2 box = circle.boundingBox();
        assertEquals(5.0, box.minX(), 1e-10);
        assertEquals(15.0, box.minY(), 1e-10);
        assertEquals(15.0, box.maxX(), 1e-10);
        assertEquals(25.0, box.maxY(), 1e-10);
    }

    @Test
    void circleDiameter() {
        Point2 center = new Point2(0, 0);
        Direction2 xDir = new Direction2(1, 0);
        Circle2 circle = new Circle2(center, xDir, 5.0);

        assertEquals(10.0, circle.diameter(), 1e-10);
    }

    @Test
    void circleCenterPoint() {
        Point2 center = new Point2(10, 20);
        Direction2 xDir = new Direction2(1, 0);
        Circle2 circle = new Circle2(center, xDir, 5.0);

        assertEquals(center, circle.centerPoint());
    }

    @Test
    void circleYDirection() {
        Point2 center = new Point2(0, 0);
        Direction2 xDir = new Direction2(1, 0);
        Circle2 circle = new Circle2(center, xDir, 5.0);

        Direction2 yDir = circle.yDirection();
        assertEquals(0.0, yDir.x(), 1e-10);
        assertEquals(1.0, yDir.y(), 1e-10);
    }

    @Test
    void circleStaticAt() {
        Circle2 circle = Circle2.at(new Point2(10, 20), 5.0);

        assertEquals(new Point2(10, 20), circle.center());
        assertEquals(5.0, circle.radius(), 1e-10);
        assertEquals(new Direction2(1, 0), circle.xDirection());
    }

    @Test
    void circleClosestPointTo() {
        Point2 center = new Point2(0, 0);
        Direction2 xDir = new Direction2(1, 0);
        Circle2 circle = new Circle2(center, xDir, 5.0);

        // Point outside circle along x-axis
        Point2 outside = new Point2(10, 0);
        Point2 closest = circle.closestPointTo(outside);
        assertEquals(5.0, closest.x(), 1e-10);
        assertEquals(0.0, closest.y(), 1e-10);

        // Point inside circle
        Point2 inside = new Point2(2, 2);
        Point2 closestInside = circle.closestPointTo(inside);
        assertTrue(circle.contains(closestInside));
    }

    @Test
    void circleDistanceTo() {
        Point2 center = new Point2(0, 0);
        Direction2 xDir = new Direction2(1, 0);
        Circle2 circle = new Circle2(center, xDir, 5.0);

        // Point outside at (10, 0) - distance = 10 - 5 = 5
        assertEquals(5.0, circle.distanceTo(new Point2(10, 0)), 1e-10);

        // Point inside at (2, 0) - distance = 5 - 2 = 3
        assertEquals(3.0, circle.distanceTo(new Point2(2, 0)), 1e-10);
    }
}