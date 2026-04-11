package com.minicad.step.model;

import java.util.List;

/**
 * Minimal parse-only CSG primitive solid.
 *
 * @param id step id
 * @param name step label
 * @param position primitive placement
 * @param dimensions primitive numeric parameters in STEP order
 * @param entityName concrete STEP entity name
 */
public record StepCsgPrimitive(
    int id, String name, StepEntity position, List<Double> dimensions, String entityName)
    implements StepEntity {

  /**
   * Creates an immutable CSG primitive record.
   */
  public StepCsgPrimitive {
    dimensions = List.copyOf(dimensions);
  }
}
