package com.minicad.step.semantic;

import com.minicad.geometry.Axis2Placement3D;
import com.minicad.geometry.CartesianPoint;
import com.minicad.geometry.Direction3;
import com.minicad.geometry.Vector3;

import java.util.Map;

/**
 * Builder for basic CAD geometry primitives extracted from StepCadBuilder.
 * 
 * This class handles construction of fundamental geometry objects:
 * - Points (CARTESIAN_POINT)
 * - Directions (DIRECTION)
 * - Vectors (VECTOR)
 * - Placements (AXIS2_PLACEMENT_3D, AXIS1_PLACEMENT)
 * 
 * Phase 1 extraction target: methods that build these basic types.
 * Caching is provided to avoid rebuilding the same geometry multiple times.
 */
final class StepCadGeometryBuilder {

  private final Map<Integer, CartesianPoint> points;
  private final Map<Integer, Direction3> directions;
  private final Map<Integer, Vector3> vectors;
  private final Map<Integer, Axis2Placement3D> placements;

  /**
   * Creates a new StepCadGeometryBuilder with the specified cache maps.
   * 
   * @param points cache for CartesianPoint objects
   * @param directions cache for Direction3 objects
   * @param vectors cache for Vector3 objects
   * @param placements cache for Axis2Placement3D objects
   */
  StepCadGeometryBuilder(
      Map<Integer, CartesianPoint> points,
      Map<Integer, Direction3> directions,
      Map<Integer, Vector3> vectors,
      Map<Integer, Axis2Placement3D> placements) {
    this.points = points;
    this.directions = directions;
    this.vectors = vectors;
    this.placements = placements;
  }

  /**
   * Builds a CartesianPoint from a STEP CARTESIAN_POINT entity.
   * 
   * @param id the STEP entity ID
   * @return the CartesianPoint geometry object
   * @throws UnsupportedOperationException - not yet implemented
   */
  CartesianPoint buildPoint(int id) {
    throw new UnsupportedOperationException("buildPoint not yet implemented");
  }

  /**
   * Builds a Direction3 from a STEP DIRECTION entity.
   * 
   * @param id the STEP entity ID
   * @return the Direction3 geometry object
   * @throws UnsupportedOperationException - not yet implemented
   */
  Direction3 buildDirection(int id) {
    throw new UnsupportedOperationException("buildDirection not yet implemented");
  }

  /**
   * Builds a Vector3 from a STEP VECTOR entity.
   * 
   * @param id the STEP entity ID
   * @return the Vector3 geometry object
   * @throws UnsupportedOperationException - not yet implemented
   */
  Vector3 buildVector(int id) {
    throw new UnsupportedOperationException("buildVector not yet implemented");
  }

  /**
   * Builds an Axis2Placement3D from a STEP AXIS2_PLACEMENT_3D entity.
   * 
   * @param id the STEP entity ID
   * @return the Axis2Placement3D geometry object
   * @throws UnsupportedOperationException - not yet implemented
   */
  Axis2Placement3D buildPlacement(int id) {
    throw new UnsupportedOperationException("buildPlacement not yet implemented");
  }

  /**
   * Builds a direction/axis from a STEP AXIS1_PLACEMENT entity.
   * 
   * @param id the STEP entity ID
   * @return the Direction3 geometry object
   * @throws UnsupportedOperationException - not yet implemented
   */
  Direction3 buildAxis1Placement(int id) {
    throw new UnsupportedOperationException("buildAxis1Placement not yet implemented");
  }
}