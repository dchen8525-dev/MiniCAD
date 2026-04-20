package com.minicad.step.model.product;

import com.minicad.step.model.base.StepEntity;
import java.util.List;

import com.minicad.step.model.geometry.StepCartesianPoint;
import com.minicad.step.model.geometry.StepAxis2Placement3D;

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
