package com.minicad.geometry;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for BSplineSurface3 class.
 */
class BSplineSurface3Test {

    @Test
    void bsplineSurfacePointAt() {
        // Simple bilinear B-spline surface (degree 1 in both directions)
        List<List<CartesianPoint>> controlPoints = List.of(
            List.of(new CartesianPoint(0, 0, 0), new CartesianPoint(0, 10, 0)),
            List.of(new CartesianPoint(10, 0, 0), new CartesianPoint(10, 10, 0))
        );
        List<Integer> uMults = List.of(2, 2);
        List<Integer> vMults = List.of(2, 2);
        List<Double> uKnots = List.of(0.0, 1.0);
        List<Double> vKnots = List.of(0.0, 1.0);

        BSplineSurface3 surface = new BSplineSurface3(1, 1, controlPoints, uMults, vMults, uKnots, vKnots);

        // Point at origin
        CartesianPoint p0 = surface.pointAt(0, 0);
        assertEquals(0.0, p0.x(), 1e-10);
        assertEquals(0.0, p0.y(), 1e-10);

        // Point at (1, 1)
        CartesianPoint p1 = surface.pointAt(1, 1);
        assertEquals(10.0, p1.x(), 1e-10);
        assertEquals(10.0, p1.y(), 1e-10);

        // Point at (0.5, 0.5)
        CartesianPoint p05 = surface.pointAt(0.5, 0.5);
        assertEquals(5.0, p05.x(), 1e-10);
        assertEquals(5.0, p05.y(), 1e-10);
    }

    @Test
    void bsplineSurfaceNormalAt() {
        List<List<CartesianPoint>> controlPoints = List.of(
            List.of(new CartesianPoint(0, 0, 0), new CartesianPoint(0, 10, 0)),
            List.of(new CartesianPoint(10, 0, 0), new CartesianPoint(10, 10, 0))
        );
        List<Integer> uMults = List.of(2, 2);
        List<Integer> vMults = List.of(2, 2);
        List<Double> uKnots = List.of(0.0, 1.0);
        List<Double> vKnots = List.of(0.0, 1.0);

        BSplineSurface3 surface = new BSplineSurface3(1, 1, controlPoints, uMults, vMults, uKnots, vKnots);

        // Normal for planar surface should be along Z
        Vector3 n = surface.normalAt(0.5, 0.5);
        assertEquals(0.0, n.x(), 1e-10);
        assertEquals(0.0, n.y(), 1e-10);
        assertTrue(Math.abs(n.z()) > 0.9);
    }

    @Test
    void bsplineSurfaceSampleGrid() {
        List<List<CartesianPoint>> controlPoints = List.of(
            List.of(new CartesianPoint(0, 0, 0), new CartesianPoint(0, 10, 0)),
            List.of(new CartesianPoint(10, 0, 0), new CartesianPoint(10, 10, 0))
        );
        List<Integer> uMults = List.of(2, 2);
        List<Integer> vMults = List.of(2, 2);
        List<Double> uKnots = List.of(0.0, 1.0);
        List<Double> vKnots = List.of(0.0, 1.0);

        BSplineSurface3 surface = new BSplineSurface3(1, 1, controlPoints, uMults, vMults, uKnots, vKnots);

        List<List<CartesianPoint>> grid = surface.sampleGrid(2, 2);
        assertEquals(3, grid.size());
        assertEquals(3, grid.get(0).size());
    }

    @Test
    void bsplineSurfaceBoundingBox() {
        List<List<CartesianPoint>> controlPoints = List.of(
            List.of(new CartesianPoint(0, 0, 0), new CartesianPoint(0, 10, 0)),
            List.of(new CartesianPoint(10, 0, 0), new CartesianPoint(10, 10, 5))
        );
        List<Integer> uMults = List.of(2, 2);
        List<Integer> vMults = List.of(2, 2);
        List<Double> uKnots = List.of(0.0, 1.0);
        List<Double> vKnots = List.of(0.0, 1.0);

        BSplineSurface3 surface = new BSplineSurface3(1, 1, controlPoints, uMults, vMults, uKnots, vKnots);

        BoundingBox3 box = surface.boundingBox();
        assertEquals(0.0, box.minX(), 1e-10);
        assertEquals(10.0, box.maxX(), 1e-10);
        assertEquals(0.0, box.minY(), 1e-10);
        assertEquals(10.0, box.maxY(), 1e-10);
    }

    @Test
    void bsplineSurfaceClosestPointTo() {
        List<List<CartesianPoint>> controlPoints = List.of(
            List.of(new CartesianPoint(0, 0, 0), new CartesianPoint(0, 10, 0)),
            List.of(new CartesianPoint(10, 0, 0), new CartesianPoint(10, 10, 0))
        );
        List<Integer> uMults = List.of(2, 2);
        List<Integer> vMults = List.of(2, 2);
        List<Double> uKnots = List.of(0.0, 1.0);
        List<Double> vKnots = List.of(0.0, 1.0);

        BSplineSurface3 surface = new BSplineSurface3(1, 1, controlPoints, uMults, vMults, uKnots, vKnots);

        CartesianPoint point = new CartesianPoint(5, 5, 5);
        CartesianPoint closest = surface.closestPointTo(point);
        assertEquals(5.0, closest.x(), 0.5);
        assertEquals(5.0, closest.y(), 0.5);
        assertEquals(0.0, closest.z(), 0.5);
    }

    @Test
    void bsplineSurfaceDistanceTo() {
        List<List<CartesianPoint>> controlPoints = List.of(
            List.of(new CartesianPoint(0, 0, 0), new CartesianPoint(0, 10, 0)),
            List.of(new CartesianPoint(10, 0, 0), new CartesianPoint(10, 10, 0))
        );
        List<Integer> uMults = List.of(2, 2);
        List<Integer> vMults = List.of(2, 2);
        List<Double> uKnots = List.of(0.0, 1.0);
        List<Double> vKnots = List.of(0.0, 1.0);

        BSplineSurface3 surface = new BSplineSurface3(1, 1, controlPoints, uMults, vMults, uKnots, vKnots);

        CartesianPoint point = new CartesianPoint(5, 5, 5);
        assertEquals(5.0, surface.distanceTo(point), 0.5);
    }

    @Test
    void bsplineSurfaceUVRange() {
        List<List<CartesianPoint>> controlPoints = List.of(
            List.of(new CartesianPoint(0, 0, 0), new CartesianPoint(0, 10, 0)),
            List.of(new CartesianPoint(10, 0, 0), new CartesianPoint(10, 10, 0))
        );
        List<Integer> uMults = List.of(2, 2);
        List<Integer> vMults = List.of(2, 2);
        List<Double> uKnots = List.of(0.0, 1.0);
        List<Double> vKnots = List.of(0.0, 1.0);

        BSplineSurface3 surface = new BSplineSurface3(1, 1, controlPoints, uMults, vMults, uKnots, vKnots);

        assertEquals(0.0, surface.uStart(), 1e-10);
        assertEquals(1.0, surface.uEnd(), 1e-10);
        assertEquals(0.0, surface.vStart(), 1e-10);
        assertEquals(1.0, surface.vEnd(), 1e-10);
    }

    @Test
    void bsplineSurfaceValidationInvalidDegree() {
        List<List<CartesianPoint>> controlPoints = List.of(
            List.of(new CartesianPoint(0, 0, 0), new CartesianPoint(0, 10, 0)),
            List.of(new CartesianPoint(10, 0, 0), new CartesianPoint(10, 10, 0))
        );
        List<Integer> uMults = List.of(2, 2);
        List<Integer> vMults = List.of(2, 2);
        List<Double> uKnots = List.of(0.0, 1.0);
        List<Double> vKnots = List.of(0.0, 1.0);

        assertThrows(Exception.class, () ->
            new BSplineSurface3(0, 1, controlPoints, uMults, vMults, uKnots, vKnots));
        assertThrows(Exception.class, () ->
            new BSplineSurface3(1, 0, controlPoints, uMults, vMults, uKnots, vKnots));
    }

    @Test
    void bsplineSurfaceValidationControlPointCount() {
        List<List<CartesianPoint>> controlPoints = List.of(
            List.of(new CartesianPoint(0, 0, 0))
        );
        List<Integer> uMults = List.of(3, 3);
        List<Integer> vMults = List.of(3, 3);
        List<Double> uKnots = List.of(0.0, 1.0);
        List<Double> vKnots = List.of(0.0, 1.0);

        assertThrows(Exception.class, () ->
            new BSplineSurface3(2, 2, controlPoints, uMults, vMults, uKnots, vKnots));
    }

    @Test
    void bsplineSurfaceQuadratic() {
        // Quadratic surface (degree 2) - simpler control point arrangement
        List<List<CartesianPoint>> controlPoints = List.of(
            List.of(new CartesianPoint(0, 0, 0), new CartesianPoint(0, 10, 0), new CartesianPoint(0, 10, 0)),
            List.of(new CartesianPoint(10, 0, 0), new CartesianPoint(10, 5, 2), new CartesianPoint(10, 10, 0)),
            List.of(new CartesianPoint(10, 0, 0), new CartesianPoint(10, 5, 0), new CartesianPoint(10, 10, 0))
        );
        List<Integer> uMults = List.of(3, 3);
        List<Integer> vMults = List.of(3, 3);
        List<Double> uKnots = List.of(0.0, 1.0);
        List<Double> vKnots = List.of(0.0, 1.0);

        BSplineSurface3 surface = new BSplineSurface3(2, 2, controlPoints, uMults, vMults, uKnots, vKnots);

        CartesianPoint p0 = surface.pointAt(0, 0);
        assertEquals(0.0, p0.x(), 1e-10);
        assertEquals(0.0, p0.y(), 1e-10);

        // End point - simpler validation
        CartesianPoint p1 = surface.pointAt(1, 1);
        assertEquals(10.0, p1.x(), 1e-10);
        assertEquals(10.0, p1.y(), 1e-10);
    }
}