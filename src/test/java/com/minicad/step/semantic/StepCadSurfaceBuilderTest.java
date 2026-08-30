package com.minicad.step.semantic;

import com.minicad.common.StepResolutionException;
import com.minicad.geometry.Axis2Placement3D;
import com.minicad.geometry.ConicalSurface;
import com.minicad.geometry.CylindricalSurface;
import com.minicad.geometry.Plane;
import com.minicad.geometry.ToroidalSurface;
import com.minicad.step.model.StepAxis2Placement3D;
import com.minicad.step.model.StepEntity;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.IntFunction;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for the analytic primitive surface builders (plane, cylinder, cone,
 * torus) that survive in StepCadSurfaceBuilder.
 */
class StepCadSurfaceBuilderTest {

    private StepCadSurfaceBuilder createMinimalBuilder(Map<Integer, StepEntity> entitiesById) {
        Map<Integer, Plane> planes = new LinkedHashMap<>();
        Map<Integer, CylindricalSurface> cylindricalSurfaces = new LinkedHashMap<>();
        Map<Integer, ConicalSurface> conicalSurfaces = new LinkedHashMap<>();
        Map<Integer, ToroidalSurface> toroidalSurfaces = new LinkedHashMap<>();

        Map<Integer, com.minicad.geometry.CartesianPoint> points = new LinkedHashMap<>();
        Map<Integer, com.minicad.geometry.Direction3> directions = new LinkedHashMap<>();
        Map<Integer, com.minicad.geometry.Vector3> vectors = new LinkedHashMap<>();
        Map<Integer, Axis2Placement3D> placements = new LinkedHashMap<>();
        Map<Integer, com.minicad.geometry.Axis1Placement> axis1Placements = new LinkedHashMap<>();
        IntFunction<com.minicad.topology.Vertex> buildVertexCallback = id -> {
            throw new UnsupportedOperationException("buildVertex not expected in this test");
        };
        StepCadGeometryBuilder geometryBuilder = new StepCadGeometryBuilder(
                entitiesById, points, directions, vectors, placements, axis1Placements, buildVertexCallback);

        IntFunction<Axis2Placement3D> buildPlacementCallback = id -> {
            StepEntity entity = entitiesById.get(id);
            if (entity instanceof StepAxis2Placement3D) {
                StepAxis2Placement3D placement = (StepAxis2Placement3D) entity;
                return new Axis2Placement3D(
                        geometryBuilder.buildPoint(placement.getLocation().id()),
                        geometryBuilder.buildDirection(placement.getAxis().id()),
                        geometryBuilder.buildDirection(placement.getRefDirection().id()));
            }
            throw new StepResolutionException("entity #" + id + " is not a AXIS2_PLACEMENT_3D");
        };

        return new StepCadSurfaceBuilder(
                entitiesById,
                planes,
                cylindricalSurfaces,
                conicalSurfaces,
                toroidalSurfaces,
                buildPlacementCallback);
    }

    @Test
    void testBuilderCreation() {
        Map<Integer, StepEntity> entitiesById = new HashMap<>();

        StepCadSurfaceBuilder builder = createMinimalBuilder(entitiesById);

        assertNotNull(builder, "Builder should be created successfully");
    }

    @Test
    void testBuildPlaneMissingEntity() {
        Map<Integer, StepEntity> entitiesById = new HashMap<>();
        StepCadSurfaceBuilder builder = createMinimalBuilder(entitiesById);

        assertThrows(StepResolutionException.class, () -> builder.buildPlane(1));
    }

    @Test
    void testBuildCylindricalSurfaceMissingEntity() {
        Map<Integer, StepEntity> entitiesById = new HashMap<>();
        StepCadSurfaceBuilder builder = createMinimalBuilder(entitiesById);

        assertThrows(StepResolutionException.class, () -> builder.buildCylindricalSurface(1));
    }

    @Test
    void testBuildConicalSurfaceMissingEntity() {
        Map<Integer, StepEntity> entitiesById = new HashMap<>();
        StepCadSurfaceBuilder builder = createMinimalBuilder(entitiesById);

        assertThrows(StepResolutionException.class, () -> builder.buildConicalSurface(1));
    }

    @Test
    void testBuildToroidalSurfaceMissingEntity() {
        Map<Integer, StepEntity> entitiesById = new HashMap<>();
        StepCadSurfaceBuilder builder = createMinimalBuilder(entitiesById);

        assertThrows(StepResolutionException.class, () -> builder.buildToroidalSurface(1));
    }

    @Test
    void testBuildDegenerateToroidalSurfaceMissingEntity() {
        Map<Integer, StepEntity> entitiesById = new HashMap<>();
        StepCadSurfaceBuilder builder = createMinimalBuilder(entitiesById);

        assertThrows(StepResolutionException.class, () -> builder.buildDegenerateToroidalSurface(1));
    }
}
