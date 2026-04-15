package com.minicad.geometry2d;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for Ellipse2 class.
 */
class Ellipse2Test {

    @Test
    void ellipsePointAt() {
        Point2 center = new Point2(0, 0);
        Direction2 xDir = new Direction2(1, 0);
        Ellipse2 ellipse = new Ellipse2(center, xDir, 4.0, 2.0);

        // Point at angle=0 should be at (semiAxis1, 0)
        Point2 p0 = ellipse.pointAt(0);
        assertEquals(4.0, p0.x(), 1e-10);
        assertEquals(0.0, p0.y(), 1e-10);

        // Point at angle=PI/2 should be at (0, semiAxis2)
        Point2 p90 = ellipse.pointAt(Math.PI / 2);
        assertEquals(0.0, p90.x(), 1e-10);
        assertEquals(2.0, p90.y(), 1e-10);

        // Point at angle=PI should be at (-semiAxis1, 0)
        Point2 p180 = ellipse.pointAt(Math.PI);
        assertEquals(-4.0, p180.x(), 1e-10);
        assertEquals(0.0, p180.y(), 1e-10);
    }

    @Test
    void ellipseContains() {
        Point2 center = new Point2(0, 0);
        Direction2 xDir = new Direction2(1, 0);
        Ellipse2 ellipse = new Ellipse2(center, xDir, 4.0, 2.0);

        assertTrue(ellipse.contains(new Point2(4, 0)));
        assertTrue(ellipse.contains(new Point2(0, 2)));
        assertTrue(ellipse.contains(new Point2(-4, 0)));
        assertFalse(ellipse.contains(new Point2(2, 1)));
    }

    @Test
    void ellipseSample() {
        Point2 center = new Point2(0, 0);
        Direction2 xDir = new Direction2(1, 0);
        Ellipse2 ellipse = new Ellipse2(center, xDir, 4.0, 2.0);

        java.util.List<Point2> samples = ellipse.sample(4);
        assertEquals(5, samples.size());

        assertEquals(4.0, samples.get(0).x(), 1e-10);
        assertEquals(0.0, samples.get(0).y(), 1e-10);
    }

    @Test
    void ellipseArcSample() {
        Point2 center = new Point2(0, 0);
        Direction2 xDir = new Direction2(1, 0);
        Ellipse2 ellipse = new Ellipse2(center, xDir, 4.0, 2.0);

        java.util.List<Point2> samples = ellipse.sample(4, 0, Math.PI / 2);
        assertEquals(5, samples.size());
        assertEquals(4.0, samples.get(0).x(), 1e-10);
        assertEquals(0.0, samples.get(4).x(), 1e-10);
        assertEquals(2.0, samples.get(4).y(), 1e-10);
    }

    @Test
    void ellipseTangentAt() {
        Point2 center = new Point2(0, 0);
        Direction2 xDir = new Direction2(1, 0);
        Ellipse2 ellipse = new Ellipse2(center, xDir, 4.0, 2.0);

        Vector2 t0 = ellipse.tangentAt(0);
        assertEquals(0.0, t0.x(), 1e-10);
        assertEquals(1.0, t0.y(), 1e-10);

        Vector2 t90 = ellipse.tangentAt(Math.PI / 2);
        assertEquals(-1.0, t90.x(), 1e-10);
        assertEquals(0.0, t90.y(), 1e-10);
    }

    @Test
    void ellipseNormalAt() {
        Point2 center = new Point2(0, 0);
        Direction2 xDir = new Direction2(1, 0);
        Ellipse2 ellipse = new Ellipse2(center, xDir, 4.0, 2.0);

        Vector2 n0 = ellipse.normalAt(0);
        assertEquals(1.0, n0.x(), 1e-10);
        assertEquals(0.0, n0.y(), 1e-10);

        Vector2 n90 = ellipse.normalAt(Math.PI / 2);
        assertEquals(0.0, n90.x(), 1e-10);
        assertEquals(1.0, n90.y(), 1e-10);
    }

    @Test
    void ellipseCurvatureAt() {
        Point2 center = new Point2(0, 0);
        Direction2 xDir = new Direction2(1, 0);
        Ellipse2 ellipse = new Ellipse2(center, xDir, 4.0, 2.0);

        // Curvature at angle=0 (major axis endpoint): k = a/b^2 = 4/4 = 1
        double k0 = ellipse.curvatureAt(0);
        assertEquals(1.0, k0, 1e-10);

        // Curvature at angle=PI/2 (minor axis endpoint): k = b/a^2 = 2/16 = 0.125
        double k90 = ellipse.curvatureAt(Math.PI / 2);
        assertEquals(0.125, k90, 1e-10);
    }

    @Test
    void ellipsePerimeter() {
        Point2 center = new Point2(0, 0);
        Direction2 xDir = new Direction2(1, 0);
        Ellipse2 ellipse = new Ellipse2(center, xDir, 4.0, 2.0);

        double perimeter = ellipse.perimeter();
        assertTrue(perimeter > 0);
        // Approximate value using Ramanujan's formula
        assertTrue(perimeter > 15 && perimeter < 25);
    }

    @Test
    void ellipseArea() {
        Point2 center = new Point2(0, 0);
        Direction2 xDir = new Direction2(1, 0);
        Ellipse2 ellipse = new Ellipse2(center, xDir, 4.0, 2.0);

        double area = ellipse.area();
        assertEquals(Math.PI * 4 * 2, area, 1e-10);  // PI * a * b
    }

    @Test
    void ellipseBoundingBox() {
        Point2 center = new Point2(0, 0);
        Direction2 xDir = new Direction2(1, 0);
        Ellipse2 ellipse = new Ellipse2(center, xDir, 4.0, 2.0);

        BoundingBox2 box = ellipse.boundingBox();
        assertEquals(-4.0, box.minX(), 1e-10);
        assertEquals(-2.0, box.minY(), 1e-10);
        assertEquals(4.0, box.maxX(), 1e-10);
        assertEquals(2.0, box.maxY(), 1e-10);
    }

    @Test
    void ellipseClosestPointTo() {
        Point2 center = new Point2(0, 0);
        Direction2 xDir = new Direction2(1, 0);
        Ellipse2 ellipse = new Ellipse2(center, xDir, 4.0, 2.0);

        Point2 outside = new Point2(10, 0);
        Point2 closest = ellipse.closestPointTo(outside);
        assertEquals(4.0, closest.x(), 0.1);
        assertEquals(0.0, closest.y(), 0.1);
    }

    @Test
    void ellipseDistanceTo() {
        Point2 center = new Point2(0, 0);
        Direction2 xDir = new Direction2(1, 0);
        Ellipse2 ellipse = new Ellipse2(center, xDir, 4.0, 2.0);

        Point2 outside = new Point2(10, 0);
        double distance = ellipse.distanceTo(outside);
        assertEquals(6.0, distance, 0.1);
    }

    @Test
    void ellipseSemiMajorMinorAxis() {
        Point2 center = new Point2(0, 0);
        Direction2 xDir = new Direction2(1, 0);
        Ellipse2 ellipse = new Ellipse2(center, xDir, 4.0, 2.0);

        assertEquals(4.0, ellipse.semiMajorAxis(), 1e-10);
        assertEquals(2.0, ellipse.semiMinorAxis(), 1e-10);

        // Reversed axes
        Ellipse2 reversed = new Ellipse2(center, xDir, 2.0, 4.0);
        assertEquals(4.0, reversed.semiMajorAxis(), 1e-10);
        assertEquals(2.0, reversed.semiMinorAxis(), 1e-10);
    }

    @Test
    void ellipseEccentricity() {
        Point2 center = new Point2(0, 0);
        Direction2 xDir = new Direction2(1, 0);
        Ellipse2 ellipse = new Ellipse2(center, xDir, 4.0, 2.0);

        // e = sqrt(1 - b^2/a^2) = sqrt(1 - 4/16) = sqrt(12/16) = sqrt(0.75)
        double expectedE = Math.sqrt(0.75);
        assertEquals(expectedE, ellipse.eccentricity(), 1e-10);

        // Circle has eccentricity 0
        Ellipse2 circle = new Ellipse2(center, xDir, 5.0, 5.0);
        assertEquals(0.0, circle.eccentricity(), 1e-10);
    }

    @Test
    void ellipseYDirection() {
        Point2 center = new Point2(0, 0);
        Direction2 xDir = new Direction2(1, 0);
        Ellipse2 ellipse = new Ellipse2(center, xDir, 4.0, 2.0);

        Direction2 yDir = ellipse.yDirection();
        assertEquals(0.0, yDir.x(), 1e-10);
        assertEquals(1.0, yDir.y(), 1e-10);
    }

    @Test
    void ellipseFoci() {
        Point2 center = new Point2(0, 0);
        Direction2 xDir = new Direction2(1, 0);
        Ellipse2 ellipse = new Ellipse2(center, xDir, 4.0, 2.0);

        // c = sqrt(a^2 - b^2) = sqrt(16 - 4) = sqrt(12) ~ 3.46
        Point2[] foci = ellipse.foci();
        assertEquals(2, foci.length);

        double c = Math.sqrt(12);
        assertEquals(-c, foci[0].x(), 0.1);
        assertEquals(c, foci[1].x(), 0.1);
    }

    @Test
    void ellipseCircleFociAtCenter() {
        Point2 center = new Point2(0, 0);
        Direction2 xDir = new Direction2(1, 0);
        Ellipse2 circle = new Ellipse2(center, xDir, 5.0, 5.0);

        Point2[] foci = circle.foci();
        assertEquals(center, foci[0]);
        assertEquals(center, foci[1]);
    }

    @Test
    void ellipseStaticAt() {
        Ellipse2 ellipse = Ellipse2.at(new Point2(10, 20), 4.0, 2.0);

        assertEquals(new Point2(10, 20), ellipse.center());
        assertEquals(4.0, ellipse.semiAxis1(), 1e-10);
        assertEquals(2.0, ellipse.semiAxis2(), 1e-10);
        assertEquals(new Direction2(1, 0), ellipse.xDirection());
    }

    @Test
    void ellipseIsCircleWhenAxesEqual() {
        Point2 center = new Point2(0, 0);
        Direction2 xDir = new Direction2(1, 0);
        Ellipse2 ellipse = new Ellipse2(center, xDir, 5.0, 5.0);

        // Perimeter should equal circumference of circle
        assertEquals(2 * Math.PI * 5, ellipse.perimeter(), 0.01);

        // Area should equal area of circle
        assertEquals(Math.PI * 25, ellipse.area(), 1e-10);

        // Bounding box should be square
        BoundingBox2 box = ellipse.boundingBox();
        assertEquals(10.0, box.width(), 1e-10);
        assertEquals(10.0, box.height(), 1e-10);
    }
}