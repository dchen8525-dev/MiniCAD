package com.minicad.step.semantic;

import com.minicad.geometry.Axis2Placement3D;
import com.minicad.geometry.CartesianPoint;
import com.minicad.geometry.Direction3;
import com.minicad.geometry.Vector3;

import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for StepCadGeometryBuilder.
 */
class StepCadGeometryBuilderTest {

  @Test
  void testBuilderCreation() {
    // Create empty cache maps
    Map<Integer, CartesianPoint> points = new HashMap<>();
    Map<Integer, Direction3> directions = new HashMap<>();
    Map<Integer, Vector3> vectors = new HashMap<>();
    Map<Integer, Axis2Placement3D> placements = new HashMap<>();

    // Create builder - should not throw
    StepCadGeometryBuilder builder = new StepCadGeometryBuilder(
        points, directions, vectors, placements);

    // Verify builder is not null
    assertNotNull(builder, "Builder should be created successfully");
  }

  @Test
  void testBuildPointThrowsUnsupportedOperation() {
    Map<Integer, CartesianPoint> points = new HashMap<>();
    Map<Integer, Direction3> directions = new HashMap<>();
    Map<Integer, Vector3> vectors = new HashMap<>();
    Map<Integer, Axis2Placement3D> placements = new HashMap<>();

    StepCadGeometryBuilder builder = new StepCadGeometryBuilder(
        points, directions, vectors, placements);

    assertThrows(UnsupportedOperationException.class, () -> builder.buildPoint(1));
  }

  @Test
  void testBuildDirectionThrowsUnsupportedOperation() {
    Map<Integer, CartesianPoint> points = new HashMap<>();
    Map<Integer, Direction3> directions = new HashMap<>();
    Map<Integer, Vector3> vectors = new HashMap<>();
    Map<Integer, Axis2Placement3D> placements = new HashMap<>();

    StepCadGeometryBuilder builder = new StepCadGeometryBuilder(
        points, directions, vectors, placements);

    assertThrows(UnsupportedOperationException.class, () -> builder.buildDirection(1));
  }

  @Test
  void testBuildVectorThrowsUnsupportedOperation() {
    Map<Integer, CartesianPoint> points = new HashMap<>();
    Map<Integer, Direction3> directions = new HashMap<>();
    Map<Integer, Vector3> vectors = new HashMap<>();
    Map<Integer, Axis2Placement3D> placements = new HashMap<>();

    StepCadGeometryBuilder builder = new StepCadGeometryBuilder(
        points, directions, vectors, placements);

    assertThrows(UnsupportedOperationException.class, () -> builder.buildVector(1));
  }

  @Test
  void testBuildPlacementThrowsUnsupportedOperation() {
    Map<Integer, CartesianPoint> points = new HashMap<>();
    Map<Integer, Direction3> directions = new HashMap<>();
    Map<Integer, Vector3> vectors = new HashMap<>();
    Map<Integer, Axis2Placement3D> placements = new HashMap<>();

    StepCadGeometryBuilder builder = new StepCadGeometryBuilder(
        points, directions, vectors, placements);

    assertThrows(UnsupportedOperationException.class, () -> builder.buildPlacement(1));
  }

  @Test
  void testBuildAxis1PlacementThrowsUnsupportedOperation() {
    Map<Integer, CartesianPoint> points = new HashMap<>();
    Map<Integer, Direction3> directions = new HashMap<>();
    Map<Integer, Vector3> vectors = new HashMap<>();
    Map<Integer, Axis2Placement3D> placements = new HashMap<>();

    StepCadGeometryBuilder builder = new StepCadGeometryBuilder(
        points, directions, vectors, placements);

    assertThrows(UnsupportedOperationException.class, () -> builder.buildAxis1Placement(1));
  }
}