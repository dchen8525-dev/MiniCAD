package com.minicad.step.model;

import java.util.List;

/**
 * Resolved POLYGONAL_BOUNDED_HALF_SPACE.
 * A half-space bounded by a polygonal face.
 *
 * @param id STEP instance id
 * @param name solid name
 * @param basisSurface the half-space surface
 * @param position placement for the polygon
 * @param polygonPoints vertices of the bounding polygon
 * @param sameSense orientation flag
 */
public record StepPolygonalBoundedHalfSpace(
    int id,
    String name,
    StepEntity basisSurface,
    StepAxis2Placement3D position,
    List<StepCartesianPoint> polygonPoints,
    boolean sameSense) implements StepEntity {

  public StepPolygonalBoundedHalfSpace {
    polygonPoints = List.copyOf(polygonPoints);
  }
}
