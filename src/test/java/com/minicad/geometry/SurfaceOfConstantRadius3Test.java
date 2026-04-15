package com.minicad.geometry;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for SurfaceOfConstantRadius3 class.
 */
class SurfaceOfConstantRadius3Test {

    @Test
    void surfaceOfConstantRadiusPointAt() {
        Axis2Placement3D position = new Axis2Placement3D(
            new CartesianPoint(0, 0, 0),
            new Direction3(0, 0, 1),
            new Direction3(1, 0, 0)
        );
        CylindricalSurface cylinder = new CylindricalSurface(position, 5.0);
        SurfaceOfConstantRadius3 surface = new SurfaceOfConstantRadius3(cylinder, 1.0);

        // Point at surface is offset from cylinder by radius along normal
        CartesianPoint p = surface.pointAt(0, 0);
        assertNotNull(p);
        // The offset should increase the radial distance
        assertTrue(p.x() > 5.0);
    }

    @Test
    void surfaceOfConstantRadiusSampleGrid() {
        Axis2Placement3D position = new Axis2Placement3D(
            new CartesianPoint(0, 0, 0),
            new Direction3(0, 0, 1),
            new Direction3(1, 0, 0)
        );
        CylindricalSurface cylinder = new CylindricalSurface(position, 5.0);
        SurfaceOfConstantRadius3 surface = new SurfaceOfConstantRadius3(cylinder, 1.0);

        java.util.List<java.util.List<CartesianPoint>> grid = surface.sampleGrid(4, 4);
        assertTrue(grid.size() > 0);
        assertTrue(grid.get(0).size() > 0);
    }

    @Test
    void surfaceOfConstantRadiusBoundingBox() {
        Axis2Placement3D position = new Axis2Placement3D(
            new CartesianPoint(0, 0, 0),
            new Direction3(0, 0, 1),
            new Direction3(1, 0, 0)
        );
        CylindricalSurface cylinder = new CylindricalSurface(position, 5.0);
        SurfaceOfConstantRadius3 surface = new SurfaceOfConstantRadius3(cylinder, 1.0);

        BoundingBox3 box = surface.boundingBox();
        // The bounding box should be expanded by the radius (1.0)
        assertTrue(box.maxX() >= 6.0);
        assertTrue(box.minX() <= -6.0);
    }

    @Test
    void surfaceOfConstantRadiusClosestPointTo() {
        Axis2Placement3D position = new Axis2Placement3D(
            new CartesianPoint(0, 0, 0),
            new Direction3(0, 0, 1),
            new Direction3(1, 0, 0)
        );
        CylindricalSurface cylinder = new CylindricalSurface(position, 5.0);
        SurfaceOfConstantRadius3 surface = new SurfaceOfConstantRadius3(cylinder, 1.0);

        CartesianPoint point = new CartesianPoint(10, 0, 0);
        CartesianPoint closest = surface.closestPointTo(point);
        assertNotNull(closest);
        assertTrue(closest.x() > 5.0);
    }

    @Test
    void surfaceOfConstantRadiusDistanceTo() {
        Axis2Placement3D position = new Axis2Placement3D(
            new CartesianPoint(0, 0, 0),
            new Direction3(0, 0, 1),
            new Direction3(1, 0, 0)
        );
        CylindricalSurface cylinder = new CylindricalSurface(position, 5.0);
        SurfaceOfConstantRadius3 surface = new SurfaceOfConstantRadius3(cylinder, 1.0);

        CartesianPoint point = new CartesianPoint(10, 0, 0);
        double distance = surface.distanceTo(point);
        assertTrue(distance > 0);
    }

    @Test
    void surfaceOfConstantRadiusWithSphericalSurface() {
        Axis2Placement3D position = new Axis2Placement3D(
            new CartesianPoint(0, 0, 0),
            new Direction3(0, 0, 1),
            new Direction3(1, 0, 0)
        );
        SphericalSurface sphere = new SphericalSurface(position, 5.0);
        SurfaceOfConstantRadius3 surface = new SurfaceOfConstantRadius3(sphere, 1.0);

        assertNotNull(surface.pointAt(0, Math.PI / 2));
        assertNotNull(surface.boundingBox());
    }

    @Test
    void surfaceOfConstantRadiusWithPlane() {
        Plane plane = new Plane(new CartesianPoint(0, 0, 0), new Direction3(0, 0, 1));
        SurfaceOfConstantRadius3 surface = new SurfaceOfConstantRadius3(plane, 1.0);

        assertNotNull(surface.pointAt(0, 0));
        assertNotNull(surface.boundingBox());
    }

    @Test
    void surfaceOfConstantRadiusValidation() {
        Axis2Placement3D position = new Axis2Placement3D(
            new CartesianPoint(0, 0, 0),
            new Direction3(0, 0, 1),
            new Direction3(1, 0, 0)
        );
        CylindricalSurface cylinder = new CylindricalSurface(position, 5.0);

        // Valid surface
        new SurfaceOfConstantRadius3(cylinder, 1.0);

        // Invalid - zero radius
        assertThrows(Exception.class, () -> new SurfaceOfConstantRadius3(cylinder, 0.0));

        // Invalid - negative radius
        assertThrows(Exception.class, () -> new SurfaceOfConstantRadius3(cylinder, -1.0));
    }
}