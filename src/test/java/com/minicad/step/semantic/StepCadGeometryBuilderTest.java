package com.minicad.step.semantic;

import com.minicad.geometry.Axis2Placement3D;
import com.minicad.geometry.CartesianPoint;
import com.minicad.geometry.Direction3;
import com.minicad.geometry.Vector3;
import com.minicad.step.model.base.StepEntity;
import com.minicad.step.model.geometry.StepCartesianPoint;
import com.minicad.topology.Vertex;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.function.IntFunction;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for StepCadGeometryBuilder.
 */
class StepCadGeometryBuilderTest {

  private StepCadGeometryBuilder createBuilder(
      Map<Integer, StepEntity> entitiesById,
      Map<Integer, CartesianPoint> points,
      Map<Integer, Direction3> directions,
      Map<Integer, Vector3> vectors,
      Map<Integer, Axis2Placement3D> placements) {
    // Create a simple mock for buildVertex callback that throws
    IntFunction<Vertex> buildVertexCallback = id -> {
      throw new UnsupportedOperationException("buildVertex not expected in this test");
    };
    return new StepCadGeometryBuilder(
        entitiesById, points, directions, vectors, placements, buildVertexCallback);
  }

  private StepCadGeometryBuilder createBuilderWithVertexCallback(
      Map<Integer, StepEntity> entitiesById,
      Map<Integer, CartesianPoint> points,
      Map<Integer, Direction3> directions,
      Map<Integer, Vector3> vectors,
      Map<Integer, Axis2Placement3D> placements,
      IntFunction<Vertex> buildVertexCallback) {
    return new StepCadGeometryBuilder(
        entitiesById, points, directions, vectors, placements, buildVertexCallback);
  }

  @Test
  void testBuilderCreation() {
    // Create empty cache maps
    Map<Integer, StepEntity> entitiesById = new HashMap<>();
    Map<Integer, CartesianPoint> points = new HashMap<>();
    Map<Integer, Direction3> directions = new HashMap<>();
    Map<Integer, Vector3> vectors = new HashMap<>();
    Map<Integer, Axis2Placement3D> placements = new HashMap<>();

    // Create builder - should not throw
    StepCadGeometryBuilder builder = createBuilder(
        entitiesById, points, directions, vectors, placements);

    // Verify builder is not null
    assertNotNull(builder, "Builder should be created successfully");
  }

  @Test
  void testBuildPointWithCartesianPoint() {
    Map<Integer, StepEntity> entitiesById = new HashMap<>();
    Map<Integer, CartesianPoint> points = new HashMap<>();
    Map<Integer, Direction3> directions = new HashMap<>();
    Map<Integer, Vector3> vectors = new HashMap<>();
    Map<Integer, Axis2Placement3D> placements = new HashMap<>();

    // Add a StepCartesianPoint to entitiesById
    StepCartesianPoint stepPoint = new StepCartesianPoint(1, "origin", Arrays.asList(10.0, 20.0, 30.0));
    entitiesById.put(1, stepPoint);

    StepCadGeometryBuilder builder = createBuilder(
        entitiesById, points, directions, vectors, placements);

    // Build the point
    CartesianPoint result = builder.buildPoint(1);

    assertNotNull(result, "Built point should not be null");
    assertEquals(10.0, result.x(), 0.001, "X coordinate should match");
    assertEquals(20.0, result.y(), 0.001, "Y coordinate should match");
    assertEquals(30.0, result.z(), 0.001, "Z coordinate should match");

    // Verify caching - same instance should be returned
    CartesianPoint cached = builder.buildPoint(1);
    assertSame(result, cached, "Same instance should be returned from cache");
  }

  @Test
  void testBuildPointWith2DCartesianPoint() {
    Map<Integer, StepEntity> entitiesById = new HashMap<>();
    Map<Integer, CartesianPoint> points = new HashMap<>();
    Map<Integer, Direction3> directions = new HashMap<>();
    Map<Integer, Vector3> vectors = new HashMap<>();
    Map<Integer, Axis2Placement3D> placements = new HashMap<>();

    // Add a 2D StepCartesianPoint to entitiesById
    StepCartesianPoint stepPoint = new StepCartesianPoint(1, "origin", Arrays.asList(5.0, 15.0));
    entitiesById.put(1, stepPoint);

    StepCadGeometryBuilder builder = createBuilder(
        entitiesById, points, directions, vectors, placements);

    // Build the point - should default z to 0.0
    CartesianPoint result = builder.buildPoint(1);

    assertNotNull(result, "Built point should not be null");
    assertEquals(5.0, result.x(), 0.001, "X coordinate should match");
    assertEquals(15.0, result.y(), 0.001, "Y coordinate should match");
    assertEquals(0.0, result.z(), 0.001, "Z coordinate should default to 0 for 2D point");
  }

  @Test
  void testBuildPointCaching() {
    Map<Integer, StepEntity> entitiesById = new HashMap<>();
    Map<Integer, CartesianPoint> points = new HashMap<>();
    Map<Integer, Direction3> directions = new HashMap<>();
    Map<Integer, Vector3> vectors = new HashMap<>();
    Map<Integer, Axis2Placement3D> placements = new HashMap<>();

    StepCartesianPoint stepPoint = new StepCartesianPoint(1, "test", Arrays.asList(1.0, 2.0, 3.0));
    entitiesById.put(1, stepPoint);

    // Pre-populate the cache
    CartesianPoint preCached = new CartesianPoint(100.0, 200.0, 300.0);
    points.put(1, preCached);

    StepCadGeometryBuilder builder = createBuilder(
        entitiesById, points, directions, vectors, placements);

    // Should return the cached value, not build a new one
    CartesianPoint result = builder.buildPoint(1);
    assertSame(preCached, result, "Should return the pre-cached instance");
  }

  @Test
  void testBuildDirectionThrowsUnsupportedOperation() {
    Map<Integer, StepEntity> entitiesById = new HashMap<>();
    Map<Integer, CartesianPoint> points = new HashMap<>();
    Map<Integer, Direction3> directions = new HashMap<>();
    Map<Integer, Vector3> vectors = new HashMap<>();
    Map<Integer, Axis2Placement3D> placements = new HashMap<>();

    StepCadGeometryBuilder builder = createBuilder(
        entitiesById, points, directions, vectors, placements);

    assertThrows(UnsupportedOperationException.class, () -> builder.buildDirection(1));
  }

  @Test
  void testBuildVectorThrowsUnsupportedOperation() {
    Map<Integer, StepEntity> entitiesById = new HashMap<>();
    Map<Integer, CartesianPoint> points = new HashMap<>();
    Map<Integer, Direction3> directions = new HashMap<>();
    Map<Integer, Vector3> vectors = new HashMap<>();
    Map<Integer, Axis2Placement3D> placements = new HashMap<>();

    StepCadGeometryBuilder builder = createBuilder(
        entitiesById, points, directions, vectors, placements);

    assertThrows(UnsupportedOperationException.class, () -> builder.buildVector(1));
  }

  @Test
  void testBuildPlacementThrowsUnsupportedOperation() {
    Map<Integer, StepEntity> entitiesById = new HashMap<>();
    Map<Integer, CartesianPoint> points = new HashMap<>();
    Map<Integer, Direction3> directions = new HashMap<>();
    Map<Integer, Vector3> vectors = new HashMap<>();
    Map<Integer, Axis2Placement3D> placements = new HashMap<>();

    StepCadGeometryBuilder builder = createBuilder(
        entitiesById, points, directions, vectors, placements);

    assertThrows(UnsupportedOperationException.class, () -> builder.buildPlacement(1));
  }

  @Test
  void testBuildAxis1PlacementThrowsUnsupportedOperation() {
    Map<Integer, StepEntity> entitiesById = new HashMap<>();
    Map<Integer, CartesianPoint> points = new HashMap<>();
    Map<Integer, Direction3> directions = new HashMap<>();
    Map<Integer, Vector3> vectors = new HashMap<>();
    Map<Integer, Axis2Placement3D> placements = new HashMap<>();

    StepCadGeometryBuilder builder = createBuilder(
        entitiesById, points, directions, vectors, placements);

    assertThrows(UnsupportedOperationException.class, () -> builder.buildAxis1Placement(1));
  }
}