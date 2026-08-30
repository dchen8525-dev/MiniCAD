package com.minicad.preview.sampling;

import com.minicad.common.UnsupportedGeometryException;
import com.minicad.geometry.BSplineSurface3;
import com.minicad.geometry.CartesianPoint;
import com.minicad.helper.StepTextReader;
import com.minicad.preview.payload.PointPayload;
import com.minicad.step.semantic.StepCadBuilder;
import com.minicad.step.semantic.StepEntityResolver;
import com.minicad.step.syntax.StepParser;
import com.minicad.step.model.StepBSplineSurfaceWithKnots;
import com.minicad.step.model.StepEntity;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Pipeline-level tests for {@link PreviewSurfaceSampler}: grid sampling of
 * B-spline surfaces built from real sample data, plus the patch/grid
 * triangulation helpers and the unsupported-type rejection path.
 */
class PreviewSurfaceSamplerTest {

    private static Map<Integer, StepEntity> resolved;
    private static StepCadBuilder builder;

    @BeforeAll
    static void buildFixture() throws Exception {
        String text = StepTextReader.readDecoded(
                Files.readAllBytes(Path.of("samples", "bspline-patch.step"))).text();
        resolved = StepEntityResolver.resolveAll(StepParser.parse(text));
        builder = StepCadBuilder.fromResolved(resolved);
    }

    private static StepBSplineSurfaceWithKnots findKnotsSurface() {
        for (StepEntity entity : resolved.values()) {
            if (entity instanceof StepBSplineSurfaceWithKnots) {
                return (StepBSplineSurfaceWithKnots) entity;
            }
        }
        return null;
    }

    @Test
    void samplesGridsFromTheSampleBsplineSurface() {
        StepBSplineSurfaceWithKnots surface = findKnotsSurface();
        assertNotNull(surface, "bspline-patch fixture should carry a B_SPLINE_SURFACE_WITH_KNOTS");

        BSplineSurface3 geometry = PreviewSurfaceSampler.buildBsplineSurface(surface, builder);
        List<java.util.List<CartesianPoint>> grid =
                PreviewSurfaceSampler.sampleSurfaceGrid(geometry, 4, 3);
        // The sampler clamps segment counts to at least 2.
        assertEquals(5, grid.size());
        assertEquals(4, grid.get(0).size());
        for (java.util.List<CartesianPoint> row : grid) {
            for (CartesianPoint point : row) {
                assertTrue(Double.isFinite(point.getX()) && Double.isFinite(point.getY()) && Double.isFinite(point.getZ()),
                        "grid points must be finite");
            }
        }
    }

    @Test
    void triangulatesASurfaceGridWithConsistentTriangleCounts() {
        List<java.util.List<CartesianPoint>> grid = new java.util.ArrayList<>();
        for (int u = 0; u <= 2; u++) {
            List<CartesianPoint> row = new java.util.ArrayList<>();
            for (int v = 0; v <= 2; v++) {
                row.add(new CartesianPoint(u, v, 0));
            }
            grid.add(row);
        }
        List<PointPayload> triangles = PreviewSurfaceSampler.triangulateSurfaceGrid(grid, true);
        // Deterministic strategy: 2x2 cells -> 8 triangles -> 24 points.
        assertEquals(24, triangles.size());
    }

    @Test
    void rejectsUnsupportedBsplineLikeSurfaces() {
        assertThrows(UnsupportedGeometryException.class,
                () -> PreviewSurfaceSampler.buildBsplineSurface(resolved.values().iterator().next(), builder));
    }
}
