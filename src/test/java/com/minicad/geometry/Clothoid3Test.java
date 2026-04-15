package com.minicad.geometry;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for Clothoid3 class.
 */
class Clothoid3Test {

    @Test
    void clothoidPointAt() {
        Axis2Placement3D position = new Axis2Placement3D(
            new CartesianPoint(0, 0, 0),
            new Direction3(0, 0, 1),
            new Direction3(1, 0, 0)
        );
        Clothoid3 clothoid = new Clothoid3(position, 1.0, 1.0);

        // Point at t=0 (start of clothoid)
        CartesianPoint p0 = clothoid.pointAt(0);
        assertEquals(0.0, p0.x(), 1e-10);
        assertEquals(0.0, p0.y(), 1e-10);

        // Point at t>0
        CartesianPoint p = clothoid.pointAt(0.5);
        assertTrue(p.x() > 0);
    }

    @Test
    void clothoidContains() {
        Axis2Placement3D position = new Axis2Placement3D(
            new CartesianPoint(0, 0, 0),
            new Direction3(0, 0, 1),
            new Direction3(1, 0, 0)
        );
        Clothoid3 clothoid = new Clothoid3(position, 1.0, 1.0);

        // Point at start
        assertTrue(clothoid.contains(new CartesianPoint(0, 0, 0)));

        // Point off the plane
        assertFalse(clothoid.contains(new CartesianPoint(0, 0, 1)));
    }

    @Test
    void clothoidSample() {
        Axis2Placement3D position = new Axis2Placement3D(
            new CartesianPoint(0, 0, 0),
            new Direction3(0, 0, 1),
            new Direction3(1, 0, 0)
        );
        Clothoid3 clothoid = new Clothoid3(position, 1.0, 1.0);

        java.util.List<CartesianPoint> samples = clothoid.sample(4);
        assertEquals(5, samples.size());
        assertEquals(0.0, samples.get(0).x(), 1e-10);
    }

    @Test
    void clothoidTangentAt() {
        Axis2Placement3D position = new Axis2Placement3D(
            new CartesianPoint(0, 0, 0),
            new Direction3(0, 0, 1),
            new Direction3(1, 0, 0)
        );
        Clothoid3 clothoid = new Clothoid3(position, 1.0, 1.0);

        // Tangent at t=0 should be along X direction
        Vector3 t0 = clothoid.tangentAt(0);
        assertEquals(1.0, t0.x(), 0.1);
        assertEquals(0.0, t0.y(), 0.1);
    }

    @Test
    void clothoidBoundingBox() {
        Axis2Placement3D position = new Axis2Placement3D(
            new CartesianPoint(0, 0, 0),
            new Direction3(0, 0, 1),
            new Direction3(1, 0, 0)
        );
        Clothoid3 clothoid = new Clothoid3(position, 1.0, 1.0);

        BoundingBox3 box = clothoid.boundingBox();
        assertTrue(box.minX() <= 0.0);
        assertTrue(box.maxX() > 0.0);
        assertEquals(0.0, box.minZ(), 1e-10);
        assertEquals(0.0, box.maxZ(), 1e-10);
    }

    @Test
    void clothoidBoundingBoxWithRange() {
        Axis2Placement3D position = new Axis2Placement3D(
            new CartesianPoint(0, 0, 0),
            new Direction3(0, 0, 1),
            new Direction3(1, 0, 0)
        );
        Clothoid3 clothoid = new Clothoid3(position, 1.0, 1.0);

        BoundingBox3 box = clothoid.boundingBox(0.0, 0.5);
        // Just verify the box is valid
        assertTrue(box.maxX() >= box.minX());
    }

    @Test
    void clothoidLength() {
        Axis2Placement3D position = new Axis2Placement3D(
            new CartesianPoint(0, 0, 0),
            new Direction3(0, 0, 1),
            new Direction3(1, 0, 0)
        );
        Clothoid3 clothoid = new Clothoid3(position, 1.0, 1.0);

        double length = clothoid.length();
        assertTrue(length > 0);
    }

    @Test
    void clothoidClosestPointTo() {
        Axis2Placement3D position = new Axis2Placement3D(
            new CartesianPoint(0, 0, 0),
            new Direction3(0, 0, 1),
            new Direction3(1, 0, 0)
        );
        Clothoid3 clothoid = new Clothoid3(position, 1.0, 1.0);

        CartesianPoint nearStart = new CartesianPoint(0.1, 0, 0);
        CartesianPoint closest = clothoid.closestPointTo(nearStart);
        assertTrue(closest.x() >= 0);
    }

    @Test
    void clothoidDistanceTo() {
        Axis2Placement3D position = new Axis2Placement3D(
            new CartesianPoint(0, 0, 0),
            new Direction3(0, 0, 1),
            new Direction3(1, 0, 0)
        );
        Clothoid3 clothoid = new Clothoid3(position, 1.0, 1.0);

        CartesianPoint atStart = new CartesianPoint(0, 0, 0);
        assertEquals(0.0, clothoid.distanceTo(atStart), 0.1);
    }

    @Test
    void clothoidParameters() {
        Axis2Placement3D position = new Axis2Placement3D(
            new CartesianPoint(0, 0, 0),
            new Direction3(0, 0, 1),
            new Direction3(1, 0, 0)
        );
        Clothoid3 clothoid = new Clothoid3(position, 2.0, 0.5);

        assertEquals(2.0, clothoid.intercept(), 1e-10);
        assertEquals(0.5, clothoid.curvatureRate(), 1e-10);
    }

    @Test
    void clothoidValidation() {
        Axis2Placement3D position = new Axis2Placement3D(
            new CartesianPoint(0, 0, 0),
            new Direction3(0, 0, 1),
            new Direction3(1, 0, 0)
        );

        // Valid clothoid
        new Clothoid3(position, 1.0, 1.0);

        // Invalid - zero curvature
        assertThrows(Exception.class, () -> new Clothoid3(position, 1.0, 0.0));
    }

    @Test
    void clothoidOffsetPosition() {
        Axis2Placement3D position = new Axis2Placement3D(
            new CartesianPoint(10, 20, 5),
            new Direction3(0, 0, 1),
            new Direction3(1, 0, 0)
        );
        Clothoid3 clothoid = new Clothoid3(position, 1.0, 1.0);

        CartesianPoint p = clothoid.pointAt(0);
        assertEquals(10.0, p.x(), 1e-10);
        assertEquals(20.0, p.y(), 1e-10);
        assertEquals(5.0, p.z(), 1e-10);
    }
}