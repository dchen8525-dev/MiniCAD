package com.minicad.step.semantic;

import com.minicad.geometry.Axis2Placement3D;
import com.minicad.geometry.BSplineCurve3;
import com.minicad.geometry.Circle;
import com.minicad.geometry.Clothoid3;
import com.minicad.geometry.CompositeCurve3;
import com.minicad.geometry.Curve3;
import com.minicad.geometry.Ellipse3;
import com.minicad.geometry.Hyperbola3;
import com.minicad.geometry.Line3;
import com.minicad.geometry.Parabola3;
import com.minicad.geometry.Polyline3;
import com.minicad.geometry.RationalBSplineCurve3;
import com.minicad.geometry.SurfaceCurve3;
import com.minicad.geometry.SurfaceGeometry;
import com.minicad.geometry.TrimmedCurve3;
import com.minicad.geometry2d.BSplineCurve2;
import com.minicad.geometry2d.Circle2;
import com.minicad.geometry2d.CompositeCurve2;
import com.minicad.geometry2d.Curve2;
import com.minicad.geometry2d.Direction2;
import com.minicad.geometry2d.Ellipse2;
import com.minicad.geometry2d.Hyperbola2;
import com.minicad.geometry2d.Line2;
import com.minicad.geometry2d.Parabola2;
import com.minicad.geometry2d.Point2;
import com.minicad.geometry2d.Polyline2;
import com.minicad.geometry2d.RationalBSplineCurve2;
import com.minicad.geometry2d.TrimmedCurve2;
import com.minicad.common.StepResolutionException;
import com.minicad.common.UnsupportedGeometryException;
import com.minicad.step.model.base.StepEntity;
import com.minicad.step.model.geometry.StepCartesianPoint;
import com.minicad.step.model.geometry.StepDirection;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.IntFunction;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for StepCadCurveBuilder.
 * 
 * Phase 1 tests focus on:
 * - Builder creation and initialization
 * - 2D Point and Direction building
 * - Basic 2D curve building (Line2, Circle2, Ellipse2, etc.)
 * - Curve caching behavior
 */
class StepCadCurveBuilderTest {

    /**
     * Creates a StepCadCurveBuilder with minimal dependencies for testing.
     */
    private StepCadCurveBuilder createMinimalBuilder(Map<Integer, StepEntity> entitiesById) {
        // Create empty 2D cache maps
        Map<Integer, Point2> points2d = new LinkedHashMap<>();
        Map<Integer, Direction2> directions2d = new LinkedHashMap<>();
        Map<Integer, Line2> lines2d = new LinkedHashMap<>();
        Map<Integer, Circle2> circles2d = new LinkedHashMap<>();
        Map<Integer, Ellipse2> ellipses2d = new LinkedHashMap<>();
        Map<Integer, Polyline2> polylines2d = new LinkedHashMap<>();
        Map<Integer, CompositeCurve2> compositeCurves2d = new LinkedHashMap<>();
        Map<Integer, BSplineCurve2> splineCurves2d = new LinkedHashMap<>();
        Map<Integer, RationalBSplineCurve2> rationalSplineCurves2d = new LinkedHashMap<>();
        Map<Integer, TrimmedCurve2> trimmedCurves2d = new LinkedHashMap<>();
        Map<Integer, Hyperbola2> hyperbolas2d = new LinkedHashMap<>();
        Map<Integer, Parabola2> parabolas2d = new LinkedHashMap<>();

        // Create empty 3D cache maps
        Map<Integer, Line3> lines3d = new LinkedHashMap<>();
        Map<Integer, Circle> circles3d = new LinkedHashMap<>();
        Map<Integer, Ellipse3> ellipses3d = new LinkedHashMap<>();
        Map<Integer, Polyline3> polylines3d = new LinkedHashMap<>();
        Map<Integer, CompositeCurve3> compositeCurves3d = new LinkedHashMap<>();
        Map<Integer, BSplineCurve3> bsplineCurves3d = new LinkedHashMap<>();
        Map<Integer, RationalBSplineCurve3> rationalBsplineCurves3d = new LinkedHashMap<>();
        Map<Integer, TrimmedCurve3> trimmedCurves3d = new LinkedHashMap<>();
        Map<Integer, SurfaceCurve3> surfaceCurves3d = new LinkedHashMap<>();
        Map<Integer, Parabola3> parabolas3d = new LinkedHashMap<>();
        Map<Integer, Hyperbola3> hyperbolas3d = new LinkedHashMap<>();
        Map<Integer, Clothoid3> clothoids3d = new LinkedHashMap<>();

        // Create placeholder dependencies
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

        StepCadGeometryOps geometryOps = new StepCadGeometryOps(null); // Will need mock for curve operations
        
        StepTrimResolver trimResolver = new StepTrimResolver(
                entitiesById, 
                id -> geometryBuilder.buildPoint(id),
                id -> new Point2(0, 0)); // Placeholder for buildPoint2

        IntFunction<Axis2Placement3D> buildPlacementCallback = id -> {
            throw new UnsupportedOperationException("buildPlacement not expected in this test");
        };

        IntFunction<SurfaceGeometry> buildSurfaceCallback = id -> {
            throw new UnsupportedOperationException("buildSurface not expected in this test");
        };

        IntFunction<Curve3> buildCurve3Callback = id -> {
            throw new UnsupportedOperationException("buildCurve3 not expected in this test");
        };

        return new StepCadCurveBuilder(
                entitiesById,
                geometryBuilder,
                geometryOps,
                trimResolver,
                points2d,
                directions2d,
                lines2d,
                circles2d,
                ellipses2d,
                polylines2d,
                compositeCurves2d,
                splineCurves2d,
                rationalSplineCurves2d,
                trimmedCurves2d,
                hyperbolas2d,
                parabolas2d,
                lines3d,
                circles3d,
                ellipses3d,
                polylines3d,
                compositeCurves3d,
                bsplineCurves3d,
                rationalBsplineCurves3d,
                trimmedCurves3d,
                surfaceCurves3d,
                parabolas3d,
                hyperbolas3d,
                clothoids3d,
                buildPlacementCallback,
                buildSurfaceCallback,
                buildCurve3Callback);
    }

    @Test
    void testBuilderCreation() {
        Map<Integer, StepEntity> entitiesById = new HashMap<>();
        
        StepCadCurveBuilder builder = createMinimalBuilder(entitiesById);
        
        assertNotNull(builder, "Builder should be created successfully");
    }

    @Test
    void testBuildPoint2WithValidEntity() {
        Map<Integer, StepEntity> entitiesById = new HashMap<>();
        
        // Add a 2D StepCartesianPoint
        StepCartesianPoint stepPoint = new StepCartesianPoint(1, "origin", Arrays.asList(10.0, 20.0));
        entitiesById.put(1, stepPoint);
        
        StepCadCurveBuilder builder = createMinimalBuilder(entitiesById);
        
        // Build the point
        Point2 result = builder.buildPoint2(1);
        
        assertNotNull(result, "Built point should not be null");
        assertEquals(10.0, result.x(), 0.001, "X coordinate should match");
        assertEquals(20.0, result.y(), 0.001, "Y coordinate should match");
    }

    @Test
    void testBuildPoint2Caching() {
        Map<Integer, StepEntity> entitiesById = new HashMap<>();
        
        StepCartesianPoint stepPoint = new StepCartesianPoint(1, "test", Arrays.asList(5.0, 15.0));
        entitiesById.put(1, stepPoint);
        
        StepCadCurveBuilder builder = createMinimalBuilder(entitiesById);
        
        // Build the point twice
        Point2 first = builder.buildPoint2(1);
        Point2 second = builder.buildPoint2(1);
        
        // Should return the same cached instance
        assertSame(first, second, "Same instance should be returned from cache");
    }

    @Test
    void testBuildPoint2WithMissingEntity() {
        Map<Integer, StepEntity> entitiesById = new HashMap<>();
        
        StepCadCurveBuilder builder = createMinimalBuilder(entitiesById);
        
        assertThrows(StepResolutionException.class, () -> builder.buildPoint2(1));
    }

    @Test
    void testBuildPoint2With3DCartesianPoint() {
        Map<Integer, StepEntity> entitiesById = new HashMap<>();
        
        // Add a 3D StepCartesianPoint (should fail for buildPoint2)
        StepCartesianPoint stepPoint = new StepCartesianPoint(1, "origin", Arrays.asList(10.0, 20.0, 30.0));
        entitiesById.put(1, stepPoint);
        
        StepCadCurveBuilder builder = createMinimalBuilder(entitiesById);
        
        assertThrows(StepResolutionException.class, () -> builder.buildPoint2(1));
    }

    @Test
    void testBuildDirection2WithValidEntity() {
        Map<Integer, StepEntity> entitiesById = new HashMap<>();
        
        // Add a 2D StepDirection
        StepDirection stepDirection = new StepDirection(1, "dir", Arrays.asList(1.0, 0.0));
        entitiesById.put(1, stepDirection);
        
        StepCadCurveBuilder builder = createMinimalBuilder(entitiesById);
        
        // Build the direction
        Direction2 result = builder.buildDirection2(1);
        
        assertNotNull(result, "Built direction should not be null");
        assertEquals(1.0, result.x(), 0.001, "X component should be 1.0");
        assertEquals(0.0, result.y(), 0.001, "Y component should be 0.0");
    }

    @Test
    void testBuildDirection2Caching() {
        Map<Integer, StepEntity> entitiesById = new HashMap<>();
        
        StepDirection stepDirection = new StepDirection(1, "dir", Arrays.asList(0.0, 1.0));
        entitiesById.put(1, stepDirection);
        
        StepCadCurveBuilder builder = createMinimalBuilder(entitiesById);
        
        // Build the direction twice
        Direction2 first = builder.buildDirection2(1);
        Direction2 second = builder.buildDirection2(1);
        
        // Should return the same cached instance
        assertSame(first, second, "Same instance should be returned from cache");
    }

    @Test
    void testBuildDirection2WithMissingEntity() {
        Map<Integer, StepEntity> entitiesById = new HashMap<>();
        
        StepCadCurveBuilder builder = createMinimalBuilder(entitiesById);
        
        assertThrows(StepResolutionException.class, () -> builder.buildDirection2(1));
    }

    @Test
    void testBuildDirection2With3DDirection() {
        Map<Integer, StepEntity> entitiesById = new HashMap<>();
        
        // Add a 3D StepDirection (should fail for buildDirection2)
        StepDirection stepDirection = new StepDirection(1, "dir", Arrays.asList(1.0, 0.0, 0.0));
        entitiesById.put(1, stepDirection);
        
        StepCadCurveBuilder builder = createMinimalBuilder(entitiesById);
        
        assertThrows(StepResolutionException.class, () -> builder.buildDirection2(1));
    }

    // ==================== Missing Entity Tests ====================
    
    // All curve methods require entities to be present in entitiesById.
    // These tests verify proper exception handling when entities are missing.

    @Test
    void testBuildLine2MissingEntity() {
        Map<Integer, StepEntity> entitiesById = new HashMap<>();
        StepCadCurveBuilder builder = createMinimalBuilder(entitiesById);
        
        assertThrows(StepResolutionException.class, () -> builder.buildLine2(1));
    }

    @Test
    void testBuildCircle2MissingEntity() {
        Map<Integer, StepEntity> entitiesById = new HashMap<>();
        StepCadCurveBuilder builder = createMinimalBuilder(entitiesById);
        
        assertThrows(StepResolutionException.class, () -> builder.buildCircle2(1));
    }

    @Test
    void testBuildEllipse2MissingEntity() {
        Map<Integer, StepEntity> entitiesById = new HashMap<>();
        StepCadCurveBuilder builder = createMinimalBuilder(entitiesById);
        
        assertThrows(StepResolutionException.class, () -> builder.buildEllipse2(1));
    }

    @Test
    void testBuildBSplineCurve2MissingEntity() {
        Map<Integer, StepEntity> entitiesById = new HashMap<>();
        StepCadCurveBuilder builder = createMinimalBuilder(entitiesById);
        
        assertThrows(StepResolutionException.class, () -> builder.buildBSplineCurve2(1));
    }

    @Test
    void testBuildPolyline2MissingEntity() {
        Map<Integer, StepEntity> entitiesById = new HashMap<>();
        StepCadCurveBuilder builder = createMinimalBuilder(entitiesById);
        
        assertThrows(StepResolutionException.class, () -> builder.buildPolyline2(1));
    }

    @Test
    void testBuildTrimmedCurve2MissingEntity() {
        Map<Integer, StepEntity> entitiesById = new HashMap<>();
        StepCadCurveBuilder builder = createMinimalBuilder(entitiesById);
        
        assertThrows(StepResolutionException.class, () -> builder.buildTrimmedCurve2(1));
    }

    @Test
    void testBuildCompositeCurve2MissingEntity() {
        Map<Integer, StepEntity> entitiesById = new HashMap<>();
        StepCadCurveBuilder builder = createMinimalBuilder(entitiesById);
        
        assertThrows(StepResolutionException.class, () -> builder.buildCompositeCurve2(1));
    }

    @Test
    void testBuildOffsetCurve2MissingEntity() {
        Map<Integer, StepEntity> entitiesById = new HashMap<>();
        StepCadCurveBuilder builder = createMinimalBuilder(entitiesById);
        
        assertThrows(StepResolutionException.class, () -> builder.buildOffsetCurve2(1));
    }

    @Test
    void testBuildCurve2UnsupportedType() {
        Map<Integer, StepEntity> entitiesById = new HashMap<>();
        
        // Add an entity that is not a supported 2D curve type
        StepCartesianPoint fakeEntity = new StepCartesianPoint(1, "fake", Arrays.asList(0.0, 0.0));
        entitiesById.put(1, fakeEntity);
        
        StepCadCurveBuilder builder = createMinimalBuilder(entitiesById);
        
        // Should throw UnsupportedGeometryException for unsupported type
        assertThrows(UnsupportedGeometryException.class, () -> builder.buildCurve2(fakeEntity));
    }

    // ==================== Phase 2 Placeholder Tests ====================

    // Note: buildCurve3 is package-private and will be fully implemented in Phase 2.
    // Testing will be done through StepCadBuilder integration tests.
}