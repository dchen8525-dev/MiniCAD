package com.minicad.app;

import com.minicad.geometry.*;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PreviewSurfaceSamplerTest {

    @Test
    void shouldSamplePlaneGrid() {
        CartesianPoint origin = new CartesianPoint(0, 0, 0);
        Direction3 normal = new Direction3(0, 0, 1);
        Plane plane = new Plane(origin, normal);

        List<List<CartesianPoint>> grid = plane.sampleGrid(4, 4);

        // sampleGrid(n, m) returns (n+1)x(m+1) grid points
        assertEquals(5, grid.size());
        assertEquals(5, grid.getFirst().size());
        // All points should be finite
        for (List<CartesianPoint> row : grid) {
            for (CartesianPoint pt : row) {
                assertNotEquals(Double.NaN, pt.x());
                assertNotEquals(Double.NaN, pt.y());
                assertNotEquals(Double.NaN, pt.z());
            }
        }
    }

    @Test
    void shouldTriangulateSurfaceGridProducesTrianglePoints() {
        CartesianPoint origin = new CartesianPoint(0, 0, 0);
        Direction3 normal = new Direction3(0, 0, 1);
        Plane plane = new Plane(origin, normal);

        List<List<CartesianPoint>> grid = plane.sampleGrid(2, 2);
        assertEquals(3, grid.size());
        assertEquals(3, grid.getFirst().size());

        List<PointPayload> triangles = PreviewSurfaceSampler.triangulateSurfaceGrid(grid, true);
        // 3x3 grid → 4 quads → 8 triangles → 24 points
        assertEquals(24, triangles.size());
    }

    @Test
    void shouldTriangulateSurfaceGridRespectsSameSense() {
        CartesianPoint origin = new CartesianPoint(0, 0, 0);
        Direction3 normal = new Direction3(0, 0, 1);
        Plane plane = new Plane(origin, normal);

        List<List<CartesianPoint>> grid = plane.sampleGrid(2, 2);
        List<PointPayload> trianglesNormal = PreviewSurfaceSampler.triangulateSurfaceGrid(grid, true);
        List<PointPayload> trianglesReversed = PreviewSurfaceSampler.triangulateSurfaceGrid(grid, false);

        assertEquals(trianglesNormal.size(), trianglesReversed.size());
        // sameSense=false should produce different vertex ordering
        assertNotEquals(trianglesNormal.get(1), trianglesReversed.get(1));
    }

    @Test
    void shouldReturnEmptyForTooSmallGrid() {
        List<List<CartesianPoint>> grid = List.of(
                List.of(new CartesianPoint(0, 0, 0))
        );
        List<PointPayload> result = PreviewSurfaceSampler.triangulateSurfaceGrid(grid, true);
        assertTrue(result.isEmpty());
    }

    @Test
    void shouldSampleCylinderGrid() {
        CartesianPoint origin = new CartesianPoint(0, 0, 0);
        Direction3 axis = new Direction3(0, 0, 1);
        Direction3 xDir = new Direction3(1, 0, 0);
        Axis2Placement3D placement = new Axis2Placement3D(origin, axis, xDir);
        CylindricalSurface cylinder = new CylindricalSurface(placement, 5.0);

        List<List<CartesianPoint>> grid = cylinder.sampleGrid(8, 8);

        assertEquals(9, grid.size());
        for (List<CartesianPoint> row : grid) {
            assertEquals(9, row.size());
        }
        // All points should be at radius 5 from the Z axis
        for (List<CartesianPoint> row : grid) {
            for (CartesianPoint pt : row) {
                double radius = Math.sqrt(pt.x() * pt.x() + pt.y() * pt.y());
                assertEquals(5.0, radius, 1e-10);
            }
        }
    }
}
