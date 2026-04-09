package com.minicad.step.model;

import java.util.List;

/**
 * Resolved POLY_LOOP.
 *
 * @param id STEP instance id
 * @param name loop name
 * @param polygon polygon points
 */
public record StepPolyLoop(int id, String name, List<StepCartesianPoint> polygon) implements StepLoop {

  public StepPolyLoop {
    polygon = List.copyOf(polygon);
  }
}
