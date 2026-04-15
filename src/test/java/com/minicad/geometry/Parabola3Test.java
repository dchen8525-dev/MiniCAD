package com.minicad.geometry;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for Parabola3 class.
 */
class Parabola3Test {

    @Test
    void parabolaPointAt() {
        Axis2Placement3D position = new Axis2Placement3D(
            new CartesianPoint(0, 0, 0),
            new Direction3(0, 0, 1),
            new Direction3(1, 0, 0)
        );
        Parabola3 parabola = new Parabola3(position, 2.0);

        // Point at t=0 (vertex)
        CartesianPoint p0 = parabola.pointAt(0);
        assertEquals(0.0, p0.x(), 1e-10);
        assertEquals(0.0, p0.y(), 1e-10);

        // Point at t=1
        // x = 2*p*t = 4, y = p*t^2 = 2
        CartesianPoint p1 = parabola.pointAt(1);
        assertEquals(4.0, p1.x(), 1e-10);
        assertEquals(2.0, p1.y(), 1e-10);

        // Point at t=-1 (symmetric)
        CartesianPoint pNeg1 = parabola.pointAt(-1);
        assertEquals(-4.0, pNeg1.x(), 1e-10);
        assertEquals(2.0, pNeg1.y(), 1e-10);
    }

    @Test
    void parabolaContains() {
        Axis2Placement3D position = new Axis2Placement3D(
            new CartesianPoint(0, 0, 0),
            new Direction3(0, 0, 1),
            new Direction3(1, 0, 0)
        );
        Parabola3 parabola = new Parabola3(position, 2.0);

        // Point at vertex (t=0)
        assertTrue(parabola.contains(new CartesianPoint(0, 0, 0)));

        // Point at t=1: (4, 2, 0)
        assertTrue(parabola.contains(new CartesianPoint(4, 2, 0)));

        // Point not on parabola
        assertFalse(parabola.contains(new CartesianPoint(1, 1, 0)));
        assertFalse(parabola.contains(new CartesianPoint(0, 0, 1)));
    }

    @Test
    void parabolaSample() {
        Axis2Placement3D position = new Axis2Placement3D(
            new CartesianPoint(0, 0, 0),
            new Direction3(0, 0, 1),
            new Direction3(1, 0, 0)
        );
        Parabola3 parabola = new Parabola3(position, 2.0);

        java.util.List<CartesianPoint> samples = parabola.sample(4);
        assertEquals(5, samples.size());

        // First point at t=-1
        assertEquals(-4.0, samples.get(0).x(), 1e-10);

        // Last point at t=1
        assertEquals(4.0, samples.get(4).x(), 1e-10);
    }

    @Test
    void parabolaSampleWithRange() {
        Axis2Placement3D position = new Axis2Placement3D(
            new CartesianPoint(0, 0, 0),
            new Direction3(0, 0, 1),
            new Direction3(1, 0, 0)
        );
        Parabola3 parabola = new Parabola3(position, 2.0);

        java.util.List<CartesianPoint> samples = parabola.sample(4, 0.0, 1.0);
        assertEquals(5, samples.size());
        assertEquals(0.0, samples.get(0).x(), 1e-10);
        assertEquals(4.0, samples.get(4).x(), 1e-10);
    }

    @Test
    void parabolaTangentAt() {
        Axis2Placement3D position = new Axis2Placement3D(
            new CartesianPoint(0, 0, 0),
            new Direction3(0, 0, 1),
            new Direction3(1, 0, 0)
        );
        Parabola3 parabola = new Parabola3(position, 2.0);

        // Tangent at vertex (t=0) is along X
        Vector3 t0 = parabola.tangentAt(0);
        assertEquals(1.0, t0.x(), 1e-10);
        assertEquals(0.0, t0.y(), 1e-10);
    }

    @Test
    void parabolaBoundingBox() {
        Axis2Placement3D position = new Axis2Placement3D(
            new CartesianPoint(0, 0, 0),
            new Direction3(0, 0, 1),
            new Direction3(1, 0, 0)
        );
        Parabola3 parabola = new Parabola3(position, 2.0);

        BoundingBox3 box = parabola.boundingBox();
        assertEquals(-4.0, box.minX(), 1e-10);
        assertEquals(4.0, box.maxX(), 1e-10);
        assertEquals(0.0, box.minY(), 1e-10);
        assertEquals(2.0, box.maxY(), 1e-10);
    }

    @Test
    void parabolaBoundingBoxWithRange() {
        Axis2Placement3D position = new Axis2Placement3D(
            new CartesianPoint(0, 0, 0),
            new Direction3(0, 0, 1),
            new Direction3(1, 0, 0)
        );
        Parabola3 parabola = new Parabola3(position, 2.0);

        BoundingBox3 box = parabola.boundingBox(0.0, 1.0);
        assertEquals(0.0, box.minX(), 1e-10);
        assertEquals(4.0, box.maxX(), 1e-10);
    }

    @Test
    void parabolaCurvatureAt() {
        Axis2Placement3D position = new Axis2Placement3D(
            new CartesianPoint(0, 0, 0),
            new Direction3(0, 0, 1),
            new Direction3(1, 0, 0)
        );
        Parabola3 parabola = new Parabola3(position, 2.0);

        // Curvature for parabola y = x^2/(4p): k = 1/(2p) = 1/4 = 0.25
        double k = parabola.curvatureAt(0);
        assertEquals(0.25, k, 1e-10);
    }

    @Test
    void parabolaClosestPointTo() {
        Axis2Placement3D position = new Axis2Placement3D(
            new CartesianPoint(0, 0, 0),
            new Direction3(0, 0, 1),
            new Direction3(1, 0, 0)
        );
        Parabola3 parabola = new Parabola3(position, 2.0);

        // Point far along X
        CartesianPoint farRight = new CartesianPoint(10, 0, 0);
        CartesianPoint closest = parabola.closestPointTo(farRight);
        assertTrue(closest.x() > 0);
    }

    @Test
    void parabolaDistanceTo() {
        Axis2Placement3D position = new Axis2Placement3D(
            new CartesianPoint(0, 0, 0),
            new Direction3(0, 0, 1),
            new Direction3(1, 0, 0)
        );
        Parabola3 parabola = new Parabola3(position, 2.0);

        CartesianPoint vertex = new CartesianPoint(0, 0, 0);
        assertEquals(0.0, parabola.distanceTo(vertex), 1e-10);
    }

    @Test
    void parabolaFocus() {
        Axis2Placement3D position = new Axis2Placement3D(
            new CartesianPoint(0, 0, 0),
            new Direction3(0, 0, 1),
            new Direction3(1, 0, 0)
        );
        Parabola3 parabola = new Parabola3(position, 2.0);

        CartesianPoint focus = parabola.focus();
        assertEquals(0.0, focus.x(), 1e-10);
        assertEquals(2.0, focus.y(), 1e-10);
    }

    @Test
    void parabolaVertex() {
        Axis2Placement3D position = new Axis2Placement3D(
            new CartesianPoint(0, 0, 0),
            new Direction3(0, 0, 1),
            new Direction3(1, 0, 0)
        );
        Parabola3 parabola = new Parabola3(position, 2.0);

        assertEquals(position.location(), parabola.vertex());
    }

    @Test
    void parabolaFocalLength() {
        Axis2Placement3D position = new Axis2Placement3D(
            new CartesianPoint(0, 0, 0),
            new Direction3(0, 0, 1),
            new Direction3(1, 0, 0)
        );
        Parabola3 parabola = new Parabola3(position, 2.0);

        assertEquals(2.0, parabola.focalLength(), 1e-10);
    }

    @Test
    void parabolaValidation() {
        Axis2Placement3D position = new Axis2Placement3D(
            new CartesianPoint(0, 0, 0),
            new Direction3(0, 0, 1),
            new Direction3(1, 0, 0)
        );

        // Valid parabola
        new Parabola3(position, 1.0);

        // Invalid - zero focal distance
        assertThrows(Exception.class, () -> new Parabola3(position, 0.0));
    }

    @Test
    void parabolaOffsetPosition() {
        Axis2Placement3D position = new Axis2Placement3D(
            new CartesianPoint(10, 20, 5),
            new Direction3(0, 0, 1),
            new Direction3(1, 0, 0)
        );
        Parabola3 parabola = new Parabola3(position, 2.0);

        CartesianPoint p = parabola.pointAt(1);
        assertEquals(14.0, p.x(), 1e-10);  // 10 + 4
        assertEquals(22.0, p.y(), 1e-10);  // 20 + 2
        assertEquals(5.0, p.z(), 1e-10);
    }
}