package com.minicad.step.model.geometry;

import com.minicad.step.model.base.StepEntity;
import java.util.List;

/**
 * Resolved POLYLINE.
 *
 * @param id STEP instance id
 * @param name polyline name
 * @param points polyline vertices
 */
public record StepPolyline(int id, String name, List<StepCartesianPoint> points)
    implements StepEntity {

  public StepPolyline {
    points = List.copyOf(points);
  }
}
