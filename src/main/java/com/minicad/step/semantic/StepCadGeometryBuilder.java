package com.minicad.step.semantic;

import com.minicad.common.StepResolutionException;
import com.minicad.common.UnsupportedGeometryException;
import com.minicad.geometry.Axis1Placement;
import com.minicad.geometry.Axis2Placement3D;
import com.minicad.geometry.CartesianPoint;
import com.minicad.geometry.Direction3;
import com.minicad.geometry.Vector3;
import com.minicad.step.model.base.StepEntity;
import com.minicad.step.model.geometry.StepAxis1Placement;
import com.minicad.step.model.geometry.StepAxis2Placement3D;
import com.minicad.step.model.geometry.StepCartesianPoint;
import com.minicad.step.model.geometry.StepDirection;
import com.minicad.step.model.geometry.StepFeaAxis2Placement3d;
import com.minicad.step.model.geometry.StepPoint;
import com.minicad.step.model.geometry.StepVector;
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
  private final Map<Integer, Axis1Placement> axis1Placements;
  private final IntFunction<Vertex> buildVertexCallback;

  /**
   * Creates a new StepCadGeometryBuilder with the specified cache maps.
   *
   * @param entitiesById map of STEP entity ID to resolved entity
   * @param points cache for CartesianPoint objects
   * @param directions cache for Direction3 objects
   * @param vectors cache for Vector3 objects
   * @param placements cache for Axis2Placement3D objects
   * @param axis1Placements cache for Axis1Placement objects
   * @param buildVertexCallback callback to build a Vertex (for StepVertexPoint handling)
   */
  StepCadGeometryBuilder(
      Map<Integer, StepEntity> entitiesById,
      Map<Integer, CartesianPoint> points,
      Map<Integer, Direction3> directions,
      Map<Integer, Vector3> vectors,
      Map<Integer, Axis2Placement3D> placements,
      Map<Integer, Axis1Placement> axis1Placements,
      IntFunction<Vertex> buildVertexCallback) {
    this.entitiesById = entitiesById;
    this.points = points;
    this.directions = directions;
    this.vectors = vectors;
    this.placements = placements;
    this.axis1Placements = axis1Placements;
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
   * Requires an entity of a specific type.
   *
   * @param id the STEP entity ID
   * @param type the expected type
   * @param expectedName name for error messages
   * @return the entity cast to the expected type
   * @throws StepResolutionException if entity not found or wrong type
   */
  private <T extends StepEntity> T requireEntity(int id, Class<T> type, String expectedName) {
    StepEntity entity = requireExistingEntity(id);
    if (!type.isInstance(entity)) {
      throw new StepResolutionException("entity #" + id + " is not a " + expectedName);
    }
    return type.cast(entity);
  }

  /**
   * Builds a Direction3 from a STEP DIRECTION entity.
   *
   * @param id the STEP entity ID
   * @return the Direction3 geometry object
   */
  Direction3 buildDirection(int id) {
    Direction3 existing = directions.get(id);
    if (existing != null) {
      return existing;
    }
    StepEntity entity = requireExistingEntity(id);
    if (!(entity instanceof StepDirection)) {
      throw new StepResolutionException("entity #" + id + " is not a DIRECTION");
    }
    StepDirection direction = (StepDirection) entity;
    if (direction.directionRatios().size() != 3) {
      throw new StepResolutionException("entity #" + id + " is not a 3D DIRECTION");
    }
    Direction3 built = Direction3.from(new Vector3(
        direction.directionRatios().get(0),
        direction.directionRatios().get(1),
        direction.directionRatios().get(2)
    ));
    directions.put(id, built);
    return built;
  }

  /**
   * Builds a Vector3 from a STEP VECTOR entity.
   *
   * @param id the STEP entity ID
   * @return the Vector3 geometry object
   */
  Vector3 buildVector(int id) {
    Vector3 existing = vectors.get(id);
    if (existing != null) {
      return existing;
    }
    StepVector vector = requireEntity(id, StepVector.class, "VECTOR");
    Vector3 built = buildDirection(vector.isOrientation().id()).asVector().scale(vector.magnitude());
    vectors.put(id, built);
    return built;
  }

  /**
   * Builds an Axis2Placement3D from a STEP AXIS2_PLACEMENT_3D entity.
   * 
   * @param id the STEP entity ID
   * @return the Axis2Placement3D geometry object
   */
  Axis2Placement3D buildPlacement(int id) {
    Axis2Placement3D existing = placements.get(id);
    if (existing != null) {
      return existing;
    }
    StepEntity entity = requireExistingEntity(id);
    if (entity instanceof StepAxis2Placement3D) {
      StepAxis2Placement3D placement = (StepAxis2Placement3D) entity;
      Axis2Placement3D built = new Axis2Placement3D(
          buildPoint(placement.getLocation().id()),
          buildDirection(placement.getAxis().id()),
          buildDirection(placement.getRefDirection().id())
      );
      placements.put(id, built);
      return built;
    }
    if (entity instanceof StepFeaAxis2Placement3d) {
      StepFeaAxis2Placement3d feaPlacement = (StepFeaAxis2Placement3d) entity;
      Axis2Placement3D built = new Axis2Placement3D(
          buildPoint(feaPlacement.getLocation().id()),
          buildDirection(feaPlacement.getAxis().id()),
          buildDirection(feaPlacement.getRefDirection().id())
      );
      placements.put(id, built);
      return built;
    }
    throw new UnsupportedGeometryException("entity #" + id + " is not a supported placement");
  }

  /**
   * Builds an Axis1Placement from a STEP AXIS1_PLACEMENT entity.
   *
   * @param id the STEP entity ID
   * @return the Axis1Placement geometry object
   */
  Axis1Placement buildAxis1Placement(int id) {
    Axis1Placement existing = axis1Placements.get(id);
    if (existing != null) {
      return existing;
    }
    StepAxis1Placement placement = requireEntity(id, StepAxis1Placement.class, "AXIS1_PLACEMENT");
    Axis1Placement built = new Axis1Placement(
        buildPoint(placement.getLocation().id()),
        buildDirection(placement.getAxis().id())
    );
    axis1Placements.put(id, built);
    return built;
  }

  /**
   * Builds an Axis2Placement3D from an Axis1Placement by deriving a reference direction.
   * Used for surfaces that need full 3D placement but only have axis defined.
   *
   * @param id the STEP entity ID
   * @return the Axis2Placement3D geometry object
   */
  Axis2Placement3D buildAxis1PlacementAsAxis2(int id) {
    Axis1Placement axis1 = buildAxis1Placement(id);
    // Derive a perpendicular reference direction
    Direction3 refDir = perpendicularDirection(axis1.getAxis());
    return new Axis2Placement3D(axis1.getLocation(), axis1.getAxis(), refDir);
  }

  /**
   * Returns a unit direction perpendicular to the given direction.
   */
  private Direction3 perpendicularDirection(Direction3 dir) {
    Vector3 v = dir.asVector();
    // Find the smallest component and cross with that axis
    if (Math.abs(v.getX()) <= Math.abs(v.getY()) && Math.abs(v.getX()) <= Math.abs(v.getZ())) {
      // Cross with X axis
      Vector3 perp = new Vector3(1, 0, 0).cross(v);
      if (perp.isZero()) {
        return new Direction3(0, 1, 0);
      }
      return Direction3.from(perp);
    } else if (Math.abs(v.getY()) <= Math.abs(v.getZ())) {
      // Cross with Y axis
      Vector3 perp = new Vector3(0, 1, 0).cross(v);
      if (perp.isZero()) {
        return new Direction3(1, 0, 0);
      }
      return Direction3.from(perp);
    } else {
      // Cross with Z axis
      Vector3 perp = new Vector3(0, 0, 1).cross(v);
      if (perp.isZero()) {
        return new Direction3(1, 0, 0);
      }
      return Direction3.from(perp);
    }
  }
}