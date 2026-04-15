package com.minicad.geometry2d;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for Hyperbola2 class.
 */
class Hyperbola2Test {

    @Test
    void hyperbolaPointAt() {
        Point2 center = new Point2(0, 0);
        Direction2 xDir = new Direction2(1, 0);
        Hyperbola2 hyperbola = new Hyperbola2(center, xDir, 4.0, 2.0);

        // Point at t=0 (at center + semiAxisA along x)
        Point2 p0 = hyperbola.pointAt(0);
        assertEquals(4.0, p0.x(), 0.1);
        assertEquals(0.0, p0.y(), 0.1);
    }

    @Test
    void hyperbolaPointAtLeftBranch() {
        Point2 center = new Point2(0, 0);
        Direction2 xDir = new Direction2(1, 0);
        Hyperbola2 hyperbola = new Hyperbola2(center, xDir, 4.0, 2.0);

        Point2 pLeft = hyperbola.pointAt(0, false);
        assertEquals(-4.0, pLeft.x(), 0.1);
        assertEquals(0.0, pLeft.y(), 0.1);
    }

    @Test
    void hyperbolaContains() {
        Point2 center = new Point2(0, 0);
        Direction2 xDir = new Direction2(1, 0);
        Hyperbola2 hyperbola = new Hyperbola2(center, xDir, 4.0, 2.0);

        assertTrue(hyperbola.contains(new Point2(4, 0)));
        assertFalse(hyperbola.contains(new Point2(2, 1)));
    }

    @Test
    void hyperbolaSample() {
        Point2 center = new Point2(0, 0);
        Direction2 xDir = new Direction2(1, 0);
        Hyperbola2 hyperbola = new Hyperbola2(center, xDir, 4.0, 2.0);

        java.util.List<Point2> samples = hyperbola.sample(4);
        assertEquals(5, samples.size());
    }

    @Test
    void hyperbolaSampleBothBranches() {
        Point2 center = new Point2(0, 0);
        Direction2 xDir = new Direction2(1, 0);
        Hyperbola2 hyperbola = new Hyperbola2(center, xDir, 4.0, 2.0);

        java.util.List<Point2> samples = hyperbola.sampleBothBranches(4, 0.0, 1.0);
        assertEquals(10, samples.size());  // 5 left + 5 right
    }

    @Test
    void hyperbolaTangentAt() {
        Point2 center = new Point2(0, 0);
        Direction2 xDir = new Direction2(1, 0);
        Hyperbola2 hyperbola = new Hyperbola2(center, xDir, 4.0, 2.0);

        Vector2 tangent = hyperbola.tangentAt(0);
        assertTrue(tangent.norm() > 0.9 && tangent.norm() < 1.1);
    }

    @Test
    void hyperbolaNormalAt() {
        Point2 center = new Point2(0, 0);
        Direction2 xDir = new Direction2(1, 0);
        Hyperbola2 hyperbola = new Hyperbola2(center, xDir, 4.0, 2.0);

        Vector2 normal = hyperbola.normalAt(0, true);
        assertTrue(normal.norm() > 0.9 && normal.norm() < 1.1);
    }

    @Test
    void hyperbolaCurvatureAt() {
        Point2 center = new Point2(0, 0);
        Direction2 xDir = new Direction2(1, 0);
        Hyperbola2 hyperbola = new Hyperbola2(center, xDir, 4.0, 2.0);

        double curvature = hyperbola.curvatureAt(0);
        assertTrue(curvature > 0);
    }

    @Test
    void hyperbolaBoundingBox() {
        Point2 center = new Point2(0, 0);
        Direction2 xDir = new Direction2(1, 0);
        Hyperbola2 hyperbola = new Hyperbola2(center, xDir, 4.0, 2.0);

        BoundingBox2 box = hyperbola.boundingBox();
        assertTrue(box.maxX() >= 4.0);
    }

    @Test
    void hyperbolaClosestPointTo() {
        Point2 center = new Point2(0, 0);
        Direction2 xDir = new Direction2(1, 0);
        Hyperbola2 hyperbola = new Hyperbola2(center, xDir, 4.0, 2.0);

        Point2 farRight = new Point2(10, 0);
        Point2 closest = hyperbola.closestPointTo(farRight);
        assertTrue(closest.x() > 0);
    }

    @Test
    void hyperbolaDistanceTo() {
        Point2 center = new Point2(0, 0);
        Direction2 xDir = new Direction2(1, 0);
        Hyperbola2 hyperbola = new Hyperbola2(center, xDir, 4.0, 2.0);

        Point2 farRight = new Point2(10, 0);
        double distance = hyperbola.distanceTo(farRight);
        assertTrue(distance > 0);
    }

    @Test
    void hyperbolaLength() {
        Point2 center = new Point2(0, 0);
        Direction2 xDir = new Direction2(1, 0);
        Hyperbola2 hyperbola = new Hyperbola2(center, xDir, 4.0, 2.0);

        double length = hyperbola.length();
        assertTrue(length > 0);
    }

    @Test
    void hyperbolaSemiAxes() {
        Point2 center = new Point2(0, 0);
        Direction2 xDir = new Direction2(1, 0);
        Hyperbola2 hyperbola = new Hyperbola2(center, xDir, 4.0, 2.0);

        assertEquals(4.0, hyperbola.semiMajorAxis(), 1e-10);
        assertEquals(2.0, hyperbola.semiMinorAxis(), 1e-10);
    }

    @Test
    void hyperbolaEccentricity() {
        Point2 center = new Point2(0, 0);
        Direction2 xDir = new Direction2(1, 0);
        Hyperbola2 hyperbola = new Hyperbola2(center, xDir, 4.0, 2.0);

        // e = sqrt(1 + b^2/a^2) = sqrt(1 + 4/16) = sqrt(1.25)
        double expectedE = Math.sqrt(1.25);
        assertEquals(expectedE, hyperbola.eccentricity(), 1e-10);
        assertTrue(hyperbola.eccentricity() >= 1.0);
    }

    @Test
    void hyperbolaFoci() {
        Point2 center = new Point2(0, 0);
        Direction2 xDir = new Direction2(1, 0);
        Hyperbola2 hyperbola = new Hyperbola2(center, xDir, 4.0, 2.0);

        // c = sqrt(a^2 + b^2) = sqrt(16 + 4) = sqrt(20) ~ 4.47
        java.util.List<Point2> foci = hyperbola.foci();
        assertEquals(2, foci.size());
        double c = Math.sqrt(20);
        // Foci are ordered along x-axis
        assertTrue(Math.abs(foci.get(0).x() - c) < 0.1 || Math.abs(foci.get(0).x() + c) < 0.1);
        assertTrue(Math.abs(foci.get(1).x() - c) < 0.1 || Math.abs(foci.get(1).x() + c) < 0.1);
    }

    @Test
    void hyperbolaYDirection() {
        Point2 center = new Point2(0, 0);
        Direction2 xDir = new Direction2(1, 0);
        Hyperbola2 hyperbola = new Hyperbola2(center, xDir, 4.0, 2.0);

        Direction2 yDir = hyperbola.yDirection();
        assertEquals(0.0, yDir.x(), 1e-10);
        assertEquals(1.0, yDir.y(), 1e-10);
    }

    @Test
    void hyperbolaStaticAt() {
        Hyperbola2 hyperbola = Hyperbola2.at(new Point2(10, 20), 4.0, 2.0);

        assertEquals(new Point2(10, 20), hyperbola.center());
        assertEquals(4.0, hyperbola.semiAxisA(), 1e-10);
        assertEquals(2.0, hyperbola.semiAxisB(), 1e-10);
    }

    @Test
    void hyperbolaValidation() {
        // Valid hyperbola
        new Hyperbola2(new Point2(0, 0), new Direction2(1, 0), 1.0, 1.0);

        // Invalid - zero semi-axis
        assertThrows(Exception.class, () -> new Hyperbola2(new Point2(0, 0), new Direction2(1, 0), 0.0, 1.0));
        assertThrows(Exception.class, () -> new Hyperbola2(new Point2(0, 0), new Direction2(1, 0), 1.0, 0.0));
    }

    @Test
    void hyperbolaOffsetCenter() {
        Point2 center = new Point2(10, 20);
        Direction2 xDir = new Direction2(1, 0);
        Hyperbola2 hyperbola = new Hyperbola2(center, xDir, 4.0, 2.0);

        Point2 p = hyperbola.pointAt(0);
        assertEquals(14.0, p.x(), 0.1);  // 10 + 4
        assertEquals(20.0, p.y(), 0.1);
    }
}