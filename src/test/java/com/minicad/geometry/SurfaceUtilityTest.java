package com.minicad.geometry;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for surface utility methods.
 */
class SurfaceUtilityTest {

    @Test
    void cylindricalSurfaceClosestPointTo() {
        Axis2Placement3D position = new Axis2Placement3D(
            new CartesianPoint(0, 0, 0),
            new Direction3(0, 0, 1),
            new Direction3(1, 0, 0)
        );
        CylindricalSurface cylinder = new CylindricalSurface(position, 5.0);

        // Point outside cylinder
        CartesianPoint outside = new CartesianPoint(10, 0, 2);
        CartesianPoint closest = cylinder.closestPointTo(outside);
        assertEquals(5.0, closest.x(), 1e-10);
        assertEquals(0.0, closest.y(), 1e-10);
        assertEquals(2.0, closest.z(), 1e-10);

        // Point inside cylinder
        CartesianPoint inside = new CartesianPoint(2, 0, 3);
        CartesianPoint closestInside = cylinder.closestPointTo(inside);
        assertEquals(5.0, closestInside.x(), 1e-10);
        assertEquals(0.0, closestInside.y(), 1e-10);
        assertEquals(3.0, closestInside.z(), 1e-10);
    }

    @Test
    void cylindricalSurfaceDistanceTo() {
        Axis2Placement3D position = new Axis2Placement3D(
            new CartesianPoint(0, 0, 0),
            new Direction3(0, 0, 1),
            new Direction3(1, 0, 0)
        );
        CylindricalSurface cylinder = new CylindricalSurface(position, 5.0);

        // Point at radius=10 (distance should be 5)
        CartesianPoint point = new CartesianPoint(10, 0, 0);
        assertEquals(5.0, cylinder.distanceTo(point), 1e-10);

        // Point at radius=3 (distance should be 2)
        CartesianPoint inside = new CartesianPoint(3, 0, 0);
        assertEquals(2.0, cylinder.distanceTo(inside), 1e-10);

        // Point on surface
        CartesianPoint onSurface = new CartesianPoint(5, 0, 0);
        assertEquals(0.0, cylinder.distanceTo(onSurface), 1e-10);
    }

    @Test
    void sphericalSurfaceClosestPointTo() {
        Axis2Placement3D position = new Axis2Placement3D(
            new CartesianPoint(0, 0, 0),
            new Direction3(0, 0, 1),
            new Direction3(1, 0, 0)
        );
        SphericalSurface sphere = new SphericalSurface(position, 5.0);

        // Point outside sphere
        CartesianPoint outside = new CartesianPoint(10, 0, 0);
        CartesianPoint closest = sphere.closestPointTo(outside);
        assertEquals(5.0, closest.x(), 1e-10);
        assertEquals(0.0, closest.y(), 1e-10);
        assertEquals(0.0, closest.z(), 1e-10);
    }

    @Test
    void sphericalSurfaceDistanceTo() {
        Axis2Placement3D position = new Axis2Placement3D(
            new CartesianPoint(0, 0, 0),
            new Direction3(0, 0, 1),
            new Direction3(1, 0, 0)
        );
        SphericalSurface sphere = new SphericalSurface(position, 5.0);

        // Point at distance 10 from center (distance to sphere = 5)
        CartesianPoint point = new CartesianPoint(10, 0, 0);
        assertEquals(5.0, sphere.distanceTo(point), 1e-10);

        // Point at distance 3 from center (distance to sphere = 2)
        CartesianPoint inside = new CartesianPoint(3, 0, 0);
        assertEquals(2.0, sphere.distanceTo(inside), 1e-10);

        // Point on surface
        CartesianPoint onSurface = new CartesianPoint(5, 0, 0);
        assertEquals(0.0, sphere.distanceTo(onSurface), 1e-10);
    }

    @Test
    void boundingBoxOfCollection() {
        java.util.List<CartesianPoint> points = java.util.List.of(
            new CartesianPoint(0, 0, 0),
            new CartesianPoint(10, 5, 3),
            new CartesianPoint(-2, 8, -1)
        );
        BoundingBox3 box = BoundingBox3.of(points);
        assertEquals(-2.0, box.minX(), 1e-10);
        assertEquals(10.0, box.maxX(), 1e-10);
        assertEquals(0.0, box.minY(), 1e-10);
        assertEquals(8.0, box.maxY(), 1e-10);
        assertEquals(-1.0, box.minZ(), 1e-10);
        assertEquals(3.0, box.maxZ(), 1e-10);
    }

    @Test
    void boundingBoxOfEmptyCollection() {
        BoundingBox3 box = BoundingBox3.of(java.util.List.of());
        assertTrue(box.isEmpty());
    }

    @Test
    void bsplineSurfaceClosestPointTo() {
        // Create a simple B-spline surface (degree 1, effectively a plane)
        java.util.List<CartesianPoint> row1 = java.util.List.of(
            new CartesianPoint(0, 0, 0),
            new CartesianPoint(10, 0, 0)
        );
        java.util.List<CartesianPoint> row2 = java.util.List.of(
            new CartesianPoint(0, 10, 0),
            new CartesianPoint(10, 10, 0)
        );
        java.util.List<java.util.List<CartesianPoint>> controlPoints = java.util.List.of(row1, row2);
        BSplineSurface3 surface = new BSplineSurface3(
            1, 1,
            controlPoints,
            java.util.List.of(2, 2),
            java.util.List.of(2, 2),
            java.util.List.of(0.0, 1.0),
            java.util.List.of(0.0, 1.0)
        );

        // Point above the surface
        CartesianPoint point = new CartesianPoint(5, 5, 10);
        CartesianPoint closest = surface.closestPointTo(point);
        assertEquals(5.0, closest.x(), 0.5);
        assertEquals(5.0, closest.y(), 0.5);
        assertEquals(0.0, closest.z(), 0.5);
    }

    @Test
    void bsplineSurfaceDistanceTo() {
        // Create a simple B-spline surface
        java.util.List<CartesianPoint> row1 = java.util.List.of(
            new CartesianPoint(0, 0, 0),
            new CartesianPoint(10, 0, 0)
        );
        java.util.List<CartesianPoint> row2 = java.util.List.of(
            new CartesianPoint(0, 10, 0),
            new CartesianPoint(10, 10, 0)
        );
        java.util.List<java.util.List<CartesianPoint>> controlPoints = java.util.List.of(row1, row2);
        BSplineSurface3 surface = new BSplineSurface3(
            1, 1,
            controlPoints,
            java.util.List.of(2, 2),
            java.util.List.of(2, 2),
            java.util.List.of(0.0, 1.0),
            java.util.List.of(0.0, 1.0)
        );

        // Point above the surface
        CartesianPoint point = new CartesianPoint(5, 5, 10);
        double distance = surface.distanceTo(point);
        assertEquals(10.0, distance, 0.5);
    }

    @Test
    void conicalSurfaceClosestPointTo() {
        Axis2Placement3D position = new Axis2Placement3D(
            new CartesianPoint(0, 0, 0),
            new Direction3(0, 0, 1),
            new Direction3(1, 0, 0)
        );
        ConicalSurface cone = new ConicalSurface(position, 5.0, Math.PI / 6);

        // Point outside cone
        CartesianPoint outside = new CartesianPoint(10, 0, 2);
        CartesianPoint closest = cone.closestPointTo(outside);
        // Should be on the cone surface
        assertTrue(closest.distanceTo(outside) > 0);
    }

    @Test
    void conicalSurfaceDistanceTo() {
        Axis2Placement3D position = new Axis2Placement3D(
            new CartesianPoint(0, 0, 0),
            new Direction3(0, 0, 1),
            new Direction3(1, 0, 0)
        );
        ConicalSurface cone = new ConicalSurface(position, 5.0, Math.PI / 6);

        CartesianPoint point = new CartesianPoint(10, 0, 0);
        double distance = cone.distanceTo(point);
        assertTrue(distance > 0);
    }

    @Test
    void toroidalSurfaceClosestPointTo() {
        Axis2Placement3D position = new Axis2Placement3D(
            new CartesianPoint(0, 0, 0),
            new Direction3(0, 0, 1),
            new Direction3(1, 0, 0)
        );
        ToroidalSurface torus = new ToroidalSurface(position, 10.0, 2.0);

        // Point outside torus
        CartesianPoint outside = new CartesianPoint(15, 0, 0);
        CartesianPoint closest = torus.closestPointTo(outside);
        // Should be on the torus surface
        assertTrue(closest.distanceTo(outside) > 0);
    }

    @Test
    void toroidalSurfaceDistanceTo() {
        Axis2Placement3D position = new Axis2Placement3D(
            new CartesianPoint(0, 0, 0),
            new Direction3(0, 0, 1),
            new Direction3(1, 0, 0)
        );
        ToroidalSurface torus = new ToroidalSurface(position, 10.0, 2.0);

        CartesianPoint point = new CartesianPoint(15, 0, 0);
        double distance = torus.distanceTo(point);
        assertTrue(distance > 0);
    }

    @Test
    void surfaceOfLinearExtrusionClosestPointTo() {
        Axis2Placement3D position = new Axis2Placement3D(
            new CartesianPoint(0, 0, 0),
            new Direction3(0, 0, 1),
            new Direction3(1, 0, 0)
        );
        Circle circle = new Circle(position, 5.0);
        Vector3 extrusionVector = new Vector3(0, 0, 10);
        SurfaceOfLinearExtrusion3 extrusion = new SurfaceOfLinearExtrusion3(circle, extrusionVector);

        // Point outside extrusion
        CartesianPoint outside = new CartesianPoint(10, 0, 5);
        CartesianPoint closest = extrusion.closestPointTo(outside);
        // Should be on the extrusion surface
        assertTrue(closest.distanceTo(outside) > 0);
    }

    @Test
    void surfaceOfLinearExtrusionDistanceTo() {
        Axis2Placement3D position = new Axis2Placement3D(
            new CartesianPoint(0, 0, 0),
            new Direction3(0, 0, 1),
            new Direction3(1, 0, 0)
        );
        Circle circle = new Circle(position, 5.0);
        Vector3 extrusionVector = new Vector3(0, 0, 10);
        SurfaceOfLinearExtrusion3 extrusion = new SurfaceOfLinearExtrusion3(circle, extrusionVector);

        CartesianPoint point = new CartesianPoint(10, 0, 5);
        double distance = extrusion.distanceTo(point);
        assertTrue(distance > 0);
    }

    @Test
    void ruledSurfaceClosestPointTo() {
        Line3 line1 = new Line3(new CartesianPoint(0, 0, 0), new Direction3(1, 0, 0));
        Line3 line2 = new Line3(new CartesianPoint(0, 10, 5), new Direction3(1, 0, 0));
        RuledSurface3 ruled = new RuledSurface3(line1, line2);

        // Point above the ruled surface
        CartesianPoint point = new CartesianPoint(5, 5, 10);
        CartesianPoint closest = ruled.closestPointTo(point);
        assertTrue(closest.distanceTo(point) > 0);
    }

    @Test
    void ruledSurfaceDistanceTo() {
        Line3 line1 = new Line3(new CartesianPoint(0, 0, 0), new Direction3(1, 0, 0));
        Line3 line2 = new Line3(new CartesianPoint(0, 10, 5), new Direction3(1, 0, 0));
        RuledSurface3 ruled = new RuledSurface3(line1, line2);

        CartesianPoint point = new CartesianPoint(5, 5, 10);
        double distance = ruled.distanceTo(point);
        assertTrue(distance > 0);
    }
}