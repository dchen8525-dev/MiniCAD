package com.minicad.step.semantic;

import com.minicad.common.StepResolutionException;
import com.minicad.common.UnsupportedGeometryException;
import com.minicad.geometry.Axis2Placement3D;
import com.minicad.geometry.CartesianPoint;
import com.minicad.geometry.Direction3;
import com.minicad.geometry.Vector3;
import com.minicad.step.model.base.StepEntity;
import com.minicad.step.model.geometry.StepCartesianPoint;
import com.minicad.step.model.geometry.StepPoint;
import com.minicad.step.model.topology.StepVertexPoint;
import com.minicad.topology.Vertex;

import java.util.Map;
import java.util.function.IntFunction;

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

  private final Map<Integer, StepEntity> entitiesById;
  private final Map<Integer, CartesianPoint> points;
  private final Map<Integer, Direction3> directions;
  private final Map<Integer, Vector3> vectors;
  private final Map<Integer, Axis2Placement3D> placements;
  private final IntFunction<Vertex> buildVertexCallback;

  /**
   * Creates a new StepCadGeometryBuilder with the specified cache maps.
   *
   * @param entitiesById map of STEP entity ID to resolved entity
   * @param points cache for CartesianPoint objects
   * @param directions cache for Direction3 objects
   * @param vectors cache for Vector3 objects
   * @param placements cache for Axis2Placement3D objects
   * @param buildVertexCallback callback to build a Vertex (for StepVertexPoint handling)
   */
  StepCadGeometryBuilder(
      Map<Integer, StepEntity> entitiesById,
      Map<Integer, CartesianPoint> points,
      Map<Integer, Direction3> directions,
      Map<Integer, Vector3> vectors,
      Map<Integer, Axis2Placement3D> placements,
      IntFunction<Vertex> buildVertexCallback) {
    this.entitiesById = entitiesById;
    this.points = points;
    this.directions = directions;
    this.vectors = vectors;
    this.placements = placements;
    this.buildVertexCallback = buildVertexCallback;
  }

  /**
   * Builds a CartesianPoint from a STEP CARTESIAN_POINT entity.
   *
   * @param id the STEP entity ID
   * @return the CartesianPoint geometry object
   */
  CartesianPoint buildPoint(int id) {
    CartesianPoint existing = points.get(id);
    if (existing != null) {
      return existing;
    }
    StepEntity entity = requireExistingEntity(id);
    if (entity instanceof StepCartesianPoint) {
      StepCartesianPoint point = (StepCartesianPoint) entity;
      CartesianPoint built = new CartesianPoint(
          point.coordinates().get(0),
          point.coordinates().get(1),
          point.coordinates().size() > 2 ? point.coordinates().get(2) : 0.0
      );
      points.put(id, built);
      return built;
    }
    if (entity instanceof StepPoint) {
      // POINT has no coordinates; return origin
      CartesianPoint built = new CartesianPoint(0.0, 0.0, 0.0);
      points.put(id, built);
      return built;
    }
    if (entity instanceof StepVertexPoint) {
      StepVertexPoint vertexPoint = (StepVertexPoint) entity;
      return buildVertexCallback.apply(vertexPoint.id()).point();
    }
    throw new UnsupportedGeometryException("entity #" + id + " is not a supported 3D point");
  }

  private StepEntity requireExistingEntity(int id) {
    StepEntity entity = entitiesById.get(id);
    if (entity == null) {
      throw new StepResolutionException("missing resolved entity #" + id);
    }
    return entity;
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