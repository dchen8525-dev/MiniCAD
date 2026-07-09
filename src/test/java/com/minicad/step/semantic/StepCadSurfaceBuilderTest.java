package com.minicad.step.semantic;

import com.minicad.geometry.Axis2Placement3D;
import com.minicad.geometry.BSplineSurface3;
import com.minicad.geometry.CartesianPoint;
import com.minicad.geometry.ConicalSurface;
import com.minicad.geometry.CylindricalSurface;
import com.minicad.geometry.Direction3;
import com.minicad.geometry.Plane;
import com.minicad.geometry.RationalBSplineSurface3;
import com.minicad.geometry.SphericalSurface;
import com.minicad.geometry.SurfaceGeometry;
import com.minicad.geometry.SurfaceOfLinearExtrusion3;
import com.minicad.geometry.SurfaceOfRevolution3;
import com.minicad.geometry.ToroidalSurface;
import com.minicad.geometry.Vector3;
import com.minicad.geometry2d.Curve2;
import com.minicad.geometry2d.Point2;
import com.minicad.common.StepResolutionException;
import com.minicad.common.UnsupportedGeometryException;
import com.minicad.step.model.StepEntity;
import com.minicad.step.model.StepAxis2Placement3D;
import com.minicad.step.model.StepCartesianPoint;
import com.minicad.step.model.StepDirection;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.IntFunction;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for StepCadSurfaceBuilder.
 *
 * Phase 1 tests focus on:
 * - Builder creation and initialization
 * - Elementary surface building (Plane, CylindricalSurface, ConicalSurface, etc.)
 * - Surface caching behavior
 * - Missing entity exception handling
 */
class StepCadSurfaceBuilderTest {

    /**
     * Creates a StepCadSurfaceBuilder with minimal dependencies for testing.
     */
    private StepCadSurfaceBuilder createMinimalBuilder(Map<Integer, StepEntity> entitiesById) {
        // Create surface cache maps
        Map<Integer, Plane> planes = new LinkedHashMap<>();
        Map<Integer, CylindricalSurface> cylindricalSurfaces = new LinkedHashMap<>();
        Map<Integer, ConicalSurface> conicalSurfaces = new LinkedHashMap<>();
        Map<Integer, ToroidalSurface> toroidalSurfaces = new LinkedHashMap<>();
        Map<Integer, SphericalSurface> sphericalSurfaces = new LinkedHashMap<>();
        Map<Integer, com.minicad.geometry.RuledSurface3> ruledSurfaces = new LinkedHashMap<>();
        Map<Integer, com.minicad.geometry.SurfaceOfConstantRadius3> constantRadiusSurfaces = new LinkedHashMap<>();
        Map<Integer, SurfaceOfLinearExtrusion3> linearExtrusionSurfaces = new LinkedHashMap<>();
        Map<Integer, SurfaceOfRevolution3> revolutionSurfaces = new LinkedHashMap<>();
        Map<Integer, com.minicad.geometry.ParaboloidSurface> paraboloidSurfaces = new LinkedHashMap<>();
        Map<Integer, com.minicad.geometry.HyperboloidSurface> hyperboloidSurfaces = new LinkedHashMap<>();
        Map<Integer, com.minicad.geometry.SurfaceOfTranslation3> translationSurfaces = new LinkedHashMap<>();
        Map<Integer, com.minicad.geometry.SurfaceOfProjection3> projectionSurfaces = new LinkedHashMap<>();
        Map<Integer, BSplineSurface3> bsplineSurfaces = new LinkedHashMap<>();
        Map<Integer, RationalBSplineSurface3> rationalBsplineSurfaces = new LinkedHashMap<>();

        // Create placeholder dependencies
        Map<Integer, CartesianPoint> points = new LinkedHashMap<>();
        Map<Integer, Direction3> directions = new LinkedHashMap<>();
        Map<Integer, Vector3> vectors = new LinkedHashMap<>();
        Map<Integer, Axis2Placement3D> placements = new LinkedHashMap<>();
        Map<Integer, com.minicad.geometry.Axis1Placement> axis1Placements = new LinkedHashMap<>();

        IntFunction<com.minicad.topology.Vertex> buildVertexCallback = id -> {
            throw new UnsupportedOperationException("buildVertex not expected in this test");
        };

        StepCadGeometryBuilder geometryBuilder = new StepCadGeometryBuilder(
                entitiesById, points, directions, vectors, placements, axis1Placements, buildVertexCallback);

        StepCadGeometryOps geometryOps = new StepCadGeometryOps(null);

        // Placeholder callbacks
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

        IntFunction<com.minicad.geometry.Axis1Placement> buildAxis1PlacementCallback = id -> {
            throw new UnsupportedOperationException("buildAxis1Placement not expected in this test");
        };

        IntFunction<com.minicad.geometry.Curve3> buildCurve3Callback = id -> {
            throw new UnsupportedOperationException("buildCurve3 not expected in this test");
        };

        IntFunction<Object> buildCurve2Callback = entity -> {
            throw new UnsupportedOperationException("buildCurve2 not expected in this test");
        };

        IntFunction<Object> buildPcurve2Callback = id -> {
            throw new UnsupportedOperationException("buildPcurve2 not expected in this test");
        };

        IntFunction<com.minicad.geometry.CompositeCurve3> buildCompositeCurveCallback = id -> {
            throw new UnsupportedOperationException("buildCompositeCurve not expected in this test");
        };

        // Placeholder curveBuilder
        StepCadCurveBuilder curveBuilder = null;

        return new StepCadSurfaceBuilder(
                entitiesById,
                geometryBuilder,
                geometryOps,
                curveBuilder,
                planes,
                cylindricalSurfaces,
                conicalSurfaces,
                toroidalSurfaces,
                sphericalSurfaces,
                ruledSurfaces,
                constantRadiusSurfaces,
                linearExtrusionSurfaces,
                revolutionSurfaces,
                paraboloidSurfaces,
                hyperboloidSurfaces,
                translationSurfaces,
                projectionSurfaces,
                bsplineSurfaces,
                rationalBsplineSurfaces,
                buildPlacementCallback,
                buildAxis1PlacementCallback,
                buildCurve3Callback,
                buildCurve2Callback,
                buildPcurve2Callback,
                buildCompositeCurveCallback);
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
    void testBuildSphericalSurfaceMissingEntity() {
        Map<Integer, StepEntity> entitiesById = new HashMap<>();
        StepCadSurfaceBuilder builder = createMinimalBuilder(entitiesById);

        assertThrows(StepResolutionException.class, () -> builder.buildSphericalSurface(1));
    }

    @Test
    void testBuildToroidalSurfaceMissingEntity() {
        Map<Integer, StepEntity> entitiesById = new HashMap<>();
        StepCadSurfaceBuilder builder = createMinimalBuilder(entitiesById);

        assertThrows(StepResolutionException.class, () -> builder.buildToroidalSurface(1));
    }

    @Test
    void testBuildBSplineSurfaceMissingEntity() {
        Map<Integer, StepEntity> entitiesById = new HashMap<>();
        StepCadSurfaceBuilder builder = createMinimalBuilder(entitiesById);

        assertThrows(StepResolutionException.class, () -> builder.buildBSplineSurface(1));
    }

    @Test
    void testBuildRationalBSplineSurfaceMissingEntity() {
        Map<Integer, StepEntity> entitiesById = new HashMap<>();
        StepCadSurfaceBuilder builder = createMinimalBuilder(entitiesById);

        assertThrows(StepResolutionException.class, () -> builder.buildRationalBSplineSurface(1));
    }

    @Test
    void testBuildSurfaceOfLinearExtrusionMissingEntity() {
        Map<Integer, StepEntity> entitiesById = new HashMap<>();
        StepCadSurfaceBuilder builder = createMinimalBuilder(entitiesById);

        assertThrows(StepResolutionException.class, () -> builder.buildSurfaceOfLinearExtrusion(1));
    }

    @Test
    void testBuildSurfaceOfRevolutionMissingEntity() {
        Map<Integer, StepEntity> entitiesById = new HashMap<>();
        StepCadSurfaceBuilder builder = createMinimalBuilder(entitiesById);

        assertThrows(StepResolutionException.class, () -> builder.buildSurfaceOfRevolution(1));
    }

    @Test
    void testBuildBezierSurfaceMissingEntity() {
        Map<Integer, StepEntity> entitiesById = new HashMap<>();
        StepCadSurfaceBuilder builder = createMinimalBuilder(entitiesById);

        assertThrows(StepResolutionException.class, () -> builder.buildBezierSurface(1));
    }

    @Test
    void testBuildUniformSurfaceMissingEntity() {
        Map<Integer, StepEntity> entitiesById = new HashMap<>();
        StepCadSurfaceBuilder builder = createMinimalBuilder(entitiesById);

        assertThrows(StepResolutionException.class, () -> builder.buildUniformSurface(1));
    }

    @Test
    void testBuildQuasiUniformSurfaceMissingEntity() {
        Map<Integer, StepEntity> entitiesById = new HashMap<>();
        StepCadSurfaceBuilder builder = createMinimalBuilder(entitiesById);

        assertThrows(StepResolutionException.class, () -> builder.buildQuasiUniformSurface(1));
    }

    @Test
    void testBuildPiecewiseBezierSurfaceMissingEntity() {
        Map<Integer, StepEntity> entitiesById = new HashMap<>();
        StepCadSurfaceBuilder builder = createMinimalBuilder(entitiesById);

        assertThrows(StepResolutionException.class, () -> builder.buildPiecewiseBezierSurface(1));
    }

    @Test
    void testBuildRectangularTrimmedSurfaceMissingEntity() {
        Map<Integer, StepEntity> entitiesById = new HashMap<>();
        StepCadSurfaceBuilder builder = createMinimalBuilder(entitiesById);

        assertThrows(StepResolutionException.class, () -> builder.buildRectangularTrimmedSurface(1));
    }

    @Test
    void testBuildCurveBoundedSurfaceMissingEntity() {
        Map<Integer, StepEntity> entitiesById = new HashMap<>();
        StepCadSurfaceBuilder builder = createMinimalBuilder(entitiesById);

        assertThrows(StepResolutionException.class, () -> builder.buildCurveBoundedSurface(1));
    }

    @Test
    void testBuildOffsetSurfaceMissingEntity() {
        Map<Integer, StepEntity> entitiesById = new HashMap<>();
        StepCadSurfaceBuilder builder = createMinimalBuilder(entitiesById);

        assertThrows(StepResolutionException.class, () -> builder.buildOffsetSurface(1));
    }

    @Test
    void testBuildParaboloidSurfaceMissingEntity() {
        Map<Integer, StepEntity> entitiesById = new HashMap<>();
        StepCadSurfaceBuilder builder = createMinimalBuilder(entitiesById);

        assertThrows(StepResolutionException.class, () -> builder.buildParaboloidSurface(1));
    }

    @Test
    void testBuildHyperboloidSurfaceMissingEntity() {
        Map<Integer, StepEntity> entitiesById = new HashMap<>();
        StepCadSurfaceBuilder builder = createMinimalBuilder(entitiesById);

        assertThrows(StepResolutionException.class, () -> builder.buildHyperboloidSurface(1));
    }

    @Test
    void testBuildSurfaceOfTranslationMissingEntity() {
        Map<Integer, StepEntity> entitiesById = new HashMap<>();
        StepCadSurfaceBuilder builder = createMinimalBuilder(entitiesById);

        assertThrows(StepResolutionException.class, () -> builder.buildSurfaceOfTranslation(1));
    }

    @Test
    void testBuildSurfaceOfProjectionMissingEntity() {
        Map<Integer, StepEntity> entitiesById = new HashMap<>();
        StepCadSurfaceBuilder builder = createMinimalBuilder(entitiesById);

        assertThrows(StepResolutionException.class, () -> builder.buildSurfaceOfProjection(1));
    }

    @Test
    void testReverseSurfaceSenseWithPlane() {
        Map<Integer, StepEntity> entitiesById = new HashMap<>();
        StepCadSurfaceBuilder builder = createMinimalBuilder(entitiesById);

        Plane plane = new Plane(new CartesianPoint(0, 0, 0), new Direction3(0, 0, 1));
        SurfaceGeometry reversed = builder.reverseSurfaceSense(plane);

        assertNotNull(reversed, "Reversed surface should not be null");
        assertTrue(reversed instanceof Plane, "Reversed surface should be a Plane");
        Plane reversedPlane = (Plane) reversed;
        assertEquals(-1, reversedPlane.getNormal().getZ(), 0.001, "Reversed normal should point down");
    }

    @Test
    void testReverseSurfaceSenseWithCylindricalSurface() {
        Map<Integer, StepEntity> entitiesById = new HashMap<>();
        StepCadSurfaceBuilder builder = createMinimalBuilder(entitiesById);

        Axis2Placement3D placement = new Axis2Placement3D(
                new CartesianPoint(0, 0, 0),
                new Direction3(0, 0, 1),
                new Direction3(1, 0, 0));
        CylindricalSurface cyl = new CylindricalSurface(placement, 10.0);
        SurfaceGeometry reversed = builder.reverseSurfaceSense(cyl);

        assertNotNull(reversed, "Reversed surface should not be null");
        assertTrue(reversed instanceof CylindricalSurface, "Reversed surface should be a CylindricalSurface");
    }

    @Test
    void testReverseSurfaceSenseWithSphericalSurface() {
        Map<Integer, StepEntity> entitiesById = new HashMap<>();
        StepCadSurfaceBuilder builder = createMinimalBuilder(entitiesById);

        Axis2Placement3D placement = new Axis2Placement3D(
                new CartesianPoint(0, 0, 0),
                new Direction3(0, 0, 1),
                new Direction3(1, 0, 0));
        SphericalSurface sphere = new SphericalSurface(placement, 10.0);
        SurfaceGeometry reversed = builder.reverseSurfaceSense(sphere);

        assertNotNull(reversed, "Reversed surface should not be null");
        assertTrue(reversed instanceof SphericalSurface, "Reversed surface should be a SphericalSurface");
    }

    @Test
    void testReverseSurfaceSenseWithToroidalSurface() {
        Map<Integer, StepEntity> entitiesById = new HashMap<>();
        StepCadSurfaceBuilder builder = createMinimalBuilder(entitiesById);

        Axis2Placement3D placement = new Axis2Placement3D(
                new CartesianPoint(0, 0, 0),
                new Direction3(0, 0, 1),
                new Direction3(1, 0, 0));
        ToroidalSurface torus = new ToroidalSurface(placement, 20.0, 5.0);
        SurfaceGeometry reversed = builder.reverseSurfaceSense(torus);

        assertNotNull(reversed, "Reversed surface should not be null");
        assertTrue(reversed instanceof ToroidalSurface, "Reversed surface should be a ToroidalSurface");
    }

    @Test
    void testEllipticalAxisSurfaceBuildersMissingEntity() {
        Map<Integer, StepEntity> entitiesById = new HashMap<>();
        StepCadSurfaceBuilder builder = createMinimalBuilder(entitiesById);

        assertThrows(StepResolutionException.class, () -> builder.buildCylindricalSurfaceWithEllipticalAxis(1));
        assertThrows(StepResolutionException.class, () -> builder.buildConicalSurfaceWithEllipticalAxis(1));
        assertThrows(StepResolutionException.class, () -> builder.buildSphericalSurfaceWithEllipticalAxis(1));
        assertThrows(StepResolutionException.class, () -> builder.buildToroidalSurfaceWithCylindricalAxis(1));
        assertThrows(StepResolutionException.class, () -> builder.buildToroidalSurfaceWithEllipticalAxis(1));
    }

    @Test
    void testBuildBSplineSurfaceWithBreakpointsMissingEntity() {
        Map<Integer, StepEntity> entitiesById = new HashMap<>();
        StepCadSurfaceBuilder builder = createMinimalBuilder(entitiesById);

        assertThrows(StepResolutionException.class, () -> builder.buildBSplineSurfaceWithBreakpoints(1));
    }

    // ==================== Integration Placeholder Tests ====================

    // Note: Full surface building tests will be added in integration tests through StepCadBuilder.
    // These tests verify proper exception handling for missing entities.
}
