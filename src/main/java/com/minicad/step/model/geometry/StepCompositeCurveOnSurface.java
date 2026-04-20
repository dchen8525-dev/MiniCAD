package com.minicad.step.model.geometry;

import com.minicad.step.model.base.StepEntity;
import java.util.List;

/**
 * Resolved COMPOSITE_CURVE_ON_SURFACE.
 *
 * @param id STEP instance id
 * @param name composite curve name
 * @param segments ordered segments
 * @param selfIntersect self intersect flag
 */
public record StepCompositeCurveOnSurface(
    int id,
    String name,
    List<StepCompositeCurveSegment> segments,
    boolean selfIntersect)
    implements StepEntity {

  public StepCompositeCurveOnSurface {
    segments = List.copyOf(segments);
  }
}
