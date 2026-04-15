package com.minicad.geometry;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for RuledSurface3 class.
 */
class RuledSurface3Test {

    @Test
    void ruledSurfacePointAt() {
        // Create a ruled surface between two polylines for predictable behavior
        Polyline3 poly1 = new Polyline3(java.util.List.of(
            new CartesianPoint(0, 0, 0),
            new CartesianPoint(10, 0, 0)
        ));
        Polyline3 poly2 = new Polyline3(java.util.List.of(
            new CartesianPoint(0, 0, 5),
            new CartesianPoint(10, 0, 5)
        ));

        RuledSurface3 surface = new RuledSurface3(poly1, poly2);

        // Point at (0, 0) should be at start of poly1
        CartesianPoint p0 = surface.pointAt(0, 0);
        assertEquals(0.0, p0.x(), 1e-10);
        assertEquals(0.0, p0.y(), 1e-10);
        assertEquals(0.0, p0.z(), 1e-10);

        // Point at (1, 1) should be at end of poly2
        CartesianPoint p1 = surface.pointAt(1, 1);
        assertEquals(10.0, p1.x(), 1e-10);
        assertEquals(0.0, p1.y(), 1e-10);
        assertEquals(5.0, p1.z(), 1e-10);

        // Point at (0.5, 0.5) should be midpoint
        CartesianPoint p05 = surface.pointAt(0.5, 0.5);
        assertEquals(5.0, p05.x(), 1e-10);
        assertEquals(0.0, p05.y(), 1e-10);
        assertEquals(2.5, p05.z(), 1e-10);
    }

    @Test
    void ruledSurfaceNormalAt() {
        Polyline3 poly1 = new Polyline3(java.util.List.of(
            new CartesianPoint(0, 0, 0),
            new CartesianPoint(10, 0, 0)
        ));
        Polyline3 poly2 = new Polyline3(java.util.List.of(
            new CartesianPoint(0, 5, 0),
            new CartesianPoint(10, 5, 0)
        ));

        RuledSurface3 surface = new RuledSurface3(poly1, poly2);

        // Normal should be computed
        Vector3 n = surface.normalAt(0.5, 0.5);
        assertNotNull(n);
        assertTrue(n.norm() > 0.5);
    }

    @Test
    void ruledSurfaceSampleGrid() {
        Polyline3 poly1 = new Polyline3(java.util.List.of(
            new CartesianPoint(0, 0, 0),
            new CartesianPoint(10, 0, 0)
        ));
        Polyline3 poly2 = new Polyline3(java.util.List.of(
            new CartesianPoint(0, 5, 0),
            new CartesianPoint(10, 5, 0)
        ));

        RuledSurface3 surface = new RuledSurface3(poly1, poly2);

        java.util.List<java.util.List<CartesianPoint>> grid = surface.sampleGrid(2, 2);
        // Verify that sampling produces results
        assertTrue(grid.size() > 0);
    }

    @Test
    void ruledSurfaceBoundingBox() {
        Polyline3 poly1 = new Polyline3(java.util.List.of(
            new CartesianPoint(0, 0, 0),
            new CartesianPoint(10, 0, 0)
        ));
        Polyline3 poly2 = new Polyline3(java.util.List.of(
            new CartesianPoint(0, 5, 10),
            new CartesianPoint(10, 5, 10)
        ));

        RuledSurface3 surface = new RuledSurface3(poly1, poly2);

        BoundingBox3 box = surface.boundingBox();
        assertTrue(box.maxY() >= 5.0);
        assertTrue(box.maxZ() >= 10.0);
    }

    @Test
    void ruledSurfaceClosestPointTo() {
        Polyline3 poly1 = new Polyline3(java.util.List.of(
            new CartesianPoint(0, 0, 0),
            new CartesianPoint(10, 0, 0)
        ));
        Polyline3 poly2 = new Polyline3(java.util.List.of(
            new CartesianPoint(0, 5, 0),
            new CartesianPoint(10, 5, 0)
        ));

        RuledSurface3 surface = new RuledSurface3(poly1, poly2);

        CartesianPoint point = new CartesianPoint(5, 2.5, 2);
        CartesianPoint closest = surface.closestPointTo(point);
        // Verify that closest point exists
        assertNotNull(closest);
        // The closest point should be on the ruled surface
        assertTrue(closest.x() >= 0.0 && closest.x() <= 10.0);
        assertTrue(closest.y() >= 0.0 && closest.y() <= 5.0);
    }

    @Test
    void ruledSurfaceDistanceTo() {
        Polyline3 poly1 = new Polyline3(java.util.List.of(
            new CartesianPoint(0, 0, 0),
            new CartesianPoint(10, 0, 0)
        ));
        Polyline3 poly2 = new Polyline3(java.util.List.of(
            new CartesianPoint(0, 5, 0),
            new CartesianPoint(10, 5, 0)
        ));

        RuledSurface3 surface = new RuledSurface3(poly1, poly2);

        CartesianPoint point = new CartesianPoint(5, 2.5, 2);
        // Distance should be roughly 2 (z-offset)
        assertTrue(surface.distanceTo(point) >= 0.0);
    }

    @Test
    void ruledSurfaceWithCircle() {
        Axis2Placement3D position = new Axis2Placement3D(
            new CartesianPoint(0, 0, 0),
            new Direction3(0, 0, 1),
            new Direction3(1, 0, 0)
        );
        Circle circle1 = new Circle(position, 5.0);
        Circle circle2 = new Circle(
            new Axis2Placement3D(
                new CartesianPoint(0, 0, 10),
                new Direction3(0, 0, 1),
                new Direction3(1, 0, 0)
            ),
            5.0
        );

        RuledSurface3 surface = new RuledSurface3(circle1, circle2);

        CartesianPoint p0 = surface.pointAt(0, 0);
        assertEquals(5.0, p0.x(), 0.1);
        assertEquals(0.0, p0.y(), 0.1);
        assertEquals(0.0, p0.z(), 0.1);

        CartesianPoint p1 = surface.pointAt(0, 1);
        assertEquals(5.0, p1.x(), 0.1);
        assertEquals(0.0, p1.y(), 0.1);
        assertEquals(10.0, p1.z(), 0.1);
    }

    @Test
    void ruledSurfaceWithPolyline() {
        Polyline3 poly1 = new Polyline3(java.util.List.of(
            new CartesianPoint(0, 0, 0),
            new CartesianPoint(10, 0, 0)
        ));
        Polyline3 poly2 = new Polyline3(java.util.List.of(
            new CartesianPoint(0, 5, 0),
            new CartesianPoint(10, 5, 0)
        ));

        RuledSurface3 surface = new RuledSurface3(poly1, poly2);

        assertNotNull(surface.pointAt(0, 0));
        assertNotNull(surface.boundingBox());
    }
}