package com.minicad.step.semantic;

import com.minicad.geometry.Axis2Placement3D;
import com.minicad.geometry.CartesianPoint;
import com.minicad.geometry.Direction3;
import com.minicad.geometry.Vector3;
import com.minicad.common.StepResolutionException;
import com.minicad.step.model.base.StepEntity;
import com.minicad.step.model.geometry.StepCartesianPoint;
import com.minicad.step.model.geometry.StepDirection;
import com.minicad.step.model.geometry.StepVector;
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
  void testBuildDirectionWithMissingEntity() {
    Map<Integer, StepEntity> entitiesById = new HashMap<>();
    Map<Integer, CartesianPoint> points = new HashMap<>();
    Map<Integer, Direction3> directions = new HashMap<>();
    Map<Integer, Vector3> vectors = new HashMap<>();
    Map<Integer, Axis2Placement3D> placements = new HashMap<>();

    StepCadGeometryBuilder builder = createBuilder(
        entitiesById, points, directions, vectors, placements);

    assertThrows(StepResolutionException.class, () -> builder.buildDirection(1));
  }

  @Test
  void testBuildDirectionWithValidEntity() {
    Map<Integer, StepEntity> entitiesById = new HashMap<>();
    Map<Integer, CartesianPoint> points = new HashMap<>();
    Map<Integer, Direction3> directions = new HashMap<>();
    Map<Integer, Vector3> vectors = new HashMap<>();
    Map<Integer, Axis2Placement3D> placements = new HashMap<>();

    // Add a StepDirection
    StepDirection stepDirection = new StepDirection(1, "dir", Arrays.asList(1.0, 0.0, 0.0));
    entitiesById.put(1, stepDirection);

    StepCadGeometryBuilder builder = createBuilder(
        entitiesById, points, directions, vectors, placements);

    // Build the direction
    Direction3 result = builder.buildDirection(1);

    assertNotNull(result, "Built direction should not be null");
    assertEquals(1.0, result.x(), 0.001, "X component should be 1.0");
    assertEquals(0.0, result.y(), 0.001, "Y component should be 0.0");
    assertEquals(0.0, result.z(), 0.001, "Z component should be 0.0");

    // Verify caching - same instance should be returned
    Direction3 cached = builder.buildDirection(1);
    assertSame(result, cached, "Same instance should be returned from cache");
  }

  @Test
  void testBuildVectorWithValidEntity() {
    Map<Integer, StepEntity> entitiesById = new HashMap<>();
    Map<Integer, CartesianPoint> points = new HashMap<>();
    Map<Integer, Direction3> directions = new HashMap<>();
    Map<Integer, Vector3> vectors = new HashMap<>();
    Map<Integer, Axis2Placement3D> placements = new HashMap<>();

    // Add a StepDirection (orientation) for the vector
    StepDirection stepDirection = new StepDirection(1, "dir", Arrays.asList(1.0, 0.0, 0.0));
    entitiesById.put(1, stepDirection);

    // Add a StepVector that references the direction
    StepVector stepVector = new StepVector(2, "vec", stepDirection, 5.0);
    entitiesById.put(2, stepVector);

    StepCadGeometryBuilder builder = createBuilder(
        entitiesById, points, directions, vectors, placements);

    // Build the vector
    Vector3 result = builder.buildVector(2);

    assertNotNull(result, "Built vector should not be null");
    // Direction (1,0,0) scaled by magnitude 5.0 should give (5,0,0)
    assertEquals(5.0, result.x(), 0.001, "X component should be 5.0");
    assertEquals(0.0, result.y(), 0.001, "Y component should be 0.0");
    assertEquals(0.0, result.z(), 0.001, "Z component should be 0.0");

    // Verify caching - same instance should be returned
    Vector3 cached = builder.buildVector(2);
    assertSame(result, cached, "Same instance should be returned from cache");
  }

  @Test
  void testBuildVectorCaching() {
    Map<Integer, StepEntity> entitiesById = new HashMap<>();
    Map<Integer, CartesianPoint> points = new HashMap<>();
    Map<Integer, Direction3> directions = new HashMap<>();
    Map<Integer, Vector3> vectors = new HashMap<>();
    Map<Integer, Axis2Placement3D> placements = new HashMap<>();

    // Add a StepDirection (orientation) for the vector
    StepDirection stepDirection = new StepDirection(1, "dir", Arrays.asList(0.0, 1.0, 0.0));
    entitiesById.put(1, stepDirection);

    // Add a StepVector that references the direction
    StepVector stepVector = new StepVector(2, "vec", stepDirection, 3.0);
    entitiesById.put(2, stepVector);

    // Pre-populate the vector cache
    Vector3 preCached = new Vector3(100.0, 200.0, 300.0);
    vectors.put(2, preCached);

    StepCadGeometryBuilder builder = createBuilder(
        entitiesById, points, directions, vectors, placements);

    // Should return the cached value, not build a new one
    Vector3 result = builder.buildVector(2);
    assertSame(preCached, result, "Should return the pre-cached instance");
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