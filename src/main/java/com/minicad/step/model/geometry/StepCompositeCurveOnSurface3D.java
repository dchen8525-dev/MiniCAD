package com.minicad.step.model.geometry;

import com.minicad.step.model.base.StepEntity;
import java.util.List;

/**
 * Resolved COMPOSITE_CURVE_ON_SURFACE_3D.
 * A composite curve that lies on a 3D surface.
 *
 * @param id STEP instance id
 * @param name curve name
 * @param segments composite curve segments
 * @param surface the surface on which the curve lies
 * @param selfIntersect whether the curve self-intersects
 */
public record StepCompositeCurveOnSurface3D(
    int id,
    String name,
    List<StepCompositeCurveSegment> segments,
    StepEntity surface,
    boolean selfIntersect) implements StepEntity {

  public StepCompositeCurveOnSurface3D {
    segments = List.copyOf(segments);
  }
}
