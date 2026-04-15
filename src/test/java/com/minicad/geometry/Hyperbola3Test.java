package com.minicad.geometry;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for Hyperbola3 class.
 */
class Hyperbola3Test {

    @Test
    void hyperbolaPointAt() {
        Axis2Placement3D position = new Axis2Placement3D(
            new CartesianPoint(0, 0, 0),
            new Direction3(0, 0, 1),
            new Direction3(1, 0, 0)
        );
        Hyperbola3 hyperbola = new Hyperbola3(position, 4.0, 2.0);

        // Point at t=1 (minimum valid parameter for right branch)
        CartesianPoint p1 = hyperbola.pointAt(1);
        assertEquals(4.0, p1.x(), 0.1);
        assertEquals(0.0, p1.y(), 0.1);

        // Point at t=2 (larger parameter)
        CartesianPoint p2 = hyperbola.pointAt(2);
        assertTrue(p2.x() > 4.0);
    }

    @Test
    void hyperbolaPointAtLeftBranch() {
        Axis2Placement3D position = new Axis2Placement3D(
            new CartesianPoint(0, 0, 0),
            new Direction3(0, 0, 1),
            new Direction3(1, 0, 0)
        );
        Hyperbola3 hyperbola = new Hyperbola3(position, 4.0, 2.0);

        // Negative t gives left branch
        CartesianPoint pLeft = hyperbola.pointAt(-1);
        assertEquals(-4.0, pLeft.x(), 0.1);
        assertEquals(0.0, pLeft.y(), 0.1);
    }

    @Test
    void hyperbolaContains() {
        Axis2Placement3D position = new Axis2Placement3D(
            new CartesianPoint(0, 0, 0),
            new Direction3(0, 0, 1),
            new Direction3(1, 0, 0)
        );
        Hyperbola3 hyperbola = new Hyperbola3(position, 4.0, 2.0);

        // Point on the hyperbola (right branch at t=1)
        assertTrue(hyperbola.contains(new CartesianPoint(4, 0, 0)));

        // Point not on the hyperbola
        assertFalse(hyperbola.contains(new CartesianPoint(2, 1, 0)));
        assertFalse(hyperbola.contains(new CartesianPoint(0, 0, 1)));
    }

    @Test
    void hyperbolaSample() {
        Axis2Placement3D position = new Axis2Placement3D(
            new CartesianPoint(0, 0, 0),
            new Direction3(0, 0, 1),
            new Direction3(1, 0, 0)
        );
        Hyperbola3 hyperbola = new Hyperbola3(position, 4.0, 2.0);

        java.util.List<CartesianPoint> samples = hyperbola.sample(4);
        assertTrue(samples.size() > 0);
    }

    @Test
    void hyperbolaSampleBranch() {
        Axis2Placement3D position = new Axis2Placement3D(
            new CartesianPoint(0, 0, 0),
            new Direction3(0, 0, 1),
            new Direction3(1, 0, 0)
        );
        Hyperbola3 hyperbola = new Hyperbola3(position, 4.0, 2.0);

        java.util.List<CartesianPoint> rightBranch = hyperbola.sampleBranch(4, 1.0, 2.0, true);
        assertEquals(5, rightBranch.size());
        assertTrue(rightBranch.get(0).x() > 0);

        java.util.List<CartesianPoint> leftBranch = hyperbola.sampleBranch(4, 1.0, 2.0, false);
        assertEquals(5, leftBranch.size());
        assertTrue(leftBranch.get(0).x() < 0);
    }

    @Test
    void hyperbolaTangentAt() {
        Axis2Placement3D position = new Axis2Placement3D(
            new CartesianPoint(0, 0, 0),
            new Direction3(0, 0, 1),
            new Direction3(1, 0, 0)
        );
        Hyperbola3 hyperbola = new Hyperbola3(position, 4.0, 2.0);

        Vector3 tangent = hyperbola.tangentAt(1);
        assertTrue(tangent.norm() > 0.9 && tangent.norm() < 1.1);
    }

    @Test
    void hyperbolaBoundingBox() {
        Axis2Placement3D position = new Axis2Placement3D(
            new CartesianPoint(0, 0, 0),
            new Direction3(0, 0, 1),
            new Direction3(1, 0, 0)
        );
        Hyperbola3 hyperbola = new Hyperbola3(position, 4.0, 2.0);

        BoundingBox3 box = hyperbola.boundingBox();
        assertTrue(box.minX() <= -4.0);
        assertTrue(box.maxX() >= 4.0);
        assertEquals(0.0, box.minZ(), 1e-10);
        assertEquals(0.0, box.maxZ(), 1e-10);
    }

    @Test
    void hyperbolaBoundingBoxWithParameterRange() {
        Axis2Placement3D position = new Axis2Placement3D(
            new CartesianPoint(0, 0, 0),
            new Direction3(0, 0, 1),
            new Direction3(1, 0, 0)
        );
        Hyperbola3 hyperbola = new Hyperbola3(position, 4.0, 2.0);

        BoundingBox3 box = hyperbola.boundingBox(1.0, 1.5);
        assertTrue(box.maxX() <= 6.1);
    }

    @Test
    void hyperbolaCurvatureAt() {
        Axis2Placement3D position = new Axis2Placement3D(
            new CartesianPoint(0, 0, 0),
            new Direction3(0, 0, 1),
            new Direction3(1, 0, 0)
        );
        Hyperbola3 hyperbola = new Hyperbola3(position, 4.0, 2.0);

        double curvature = hyperbola.curvatureAt(1);
        assertTrue(curvature > 0);
    }

    @Test
    void hyperbolaClosestPointTo() {
        Axis2Placement3D position = new Axis2Placement3D(
            new CartesianPoint(0, 0, 0),
            new Direction3(0, 0, 1),
            new Direction3(1, 0, 0)
        );
        Hyperbola3 hyperbola = new Hyperbola3(position, 4.0, 2.0);

        CartesianPoint farRight = new CartesianPoint(10, 0, 0);
        CartesianPoint closest = hyperbola.closestPointTo(farRight);
        assertTrue(closest.x() > 0);

        CartesianPoint farLeft = new CartesianPoint(-10, 0, 0);
        CartesianPoint closestLeft = hyperbola.closestPointTo(farLeft);
        assertTrue(closestLeft.x() < 0);
    }

    @Test
    void hyperbolaDistanceTo() {
        Axis2Placement3D position = new Axis2Placement3D(
            new CartesianPoint(0, 0, 0),
            new Direction3(0, 0, 1),
            new Direction3(1, 0, 0)
        );
        Hyperbola3 hyperbola = new Hyperbola3(position, 4.0, 2.0);

        CartesianPoint farRight = new CartesianPoint(10, 0, 0);
        double distance = hyperbola.distanceTo(farRight);
        assertTrue(distance > 0);
    }

    @Test
    void hyperbolaSemiAxes() {
        Axis2Placement3D position = new Axis2Placement3D(
            new CartesianPoint(0, 0, 0),
            new Direction3(0, 0, 1),
            new Direction3(1, 0, 0)
        );
        Hyperbola3 hyperbola = new Hyperbola3(position, 4.0, 2.0);

        assertEquals(4.0, hyperbola.semiMajorAxis(), 1e-10);
        assertEquals(2.0, hyperbola.semiMinorAxis(), 1e-10);
    }

    @Test
    void hyperbolaEccentricity() {
        Axis2Placement3D position = new Axis2Placement3D(
            new CartesianPoint(0, 0, 0),
            new Direction3(0, 0, 1),
            new Direction3(1, 0, 0)
        );
        Hyperbola3 hyperbola = new Hyperbola3(position, 4.0, 2.0);

        // e = sqrt(1 + b^2/a^2) = sqrt(1 + 4/16) = sqrt(1.25)
        double expectedE = Math.sqrt(1.25);
        assertEquals(expectedE, hyperbola.eccentricity(), 1e-10);
        assertTrue(hyperbola.eccentricity() >= 1.0);
    }

    @Test
    void hyperbolaValidation() {
        Axis2Placement3D position = new Axis2Placement3D(
            new CartesianPoint(0, 0, 0),
            new Direction3(0, 0, 1),
            new Direction3(1, 0, 0)
        );

        // Valid hyperbola
        new Hyperbola3(position, 1.0, 1.0);

        // Invalid - zero semi-axis
        assertThrows(Exception.class, () -> new Hyperbola3(position, 0.0, 1.0));
        assertThrows(Exception.class, () -> new Hyperbola3(position, 1.0, 0.0));
    }

    @Test
    void hyperbolaOffsetPosition() {
        Axis2Placement3D position = new Axis2Placement3D(
            new CartesianPoint(10, 20, 5),
            new Direction3(0, 0, 1),
            new Direction3(1, 0, 0)
        );
        Hyperbola3 hyperbola = new Hyperbola3(position, 4.0, 2.0);

        CartesianPoint p = hyperbola.pointAt(1);
        assertEquals(10.0 + 4.0, p.x(), 0.1);
        assertEquals(20.0, p.y(), 0.1);
        assertEquals(5.0, p.z(), 1e-10);
    }
}