package com.minicad.step.model;

import java.util.List;

/**
 * Resolved SHAPE_DIMENSION_REPRESENTATION.
 * A representation of dimensional information for a shape.
 *
 * @param id STEP instance id
 * @param name representation name
 * @param items dimension items
 * @param context representation context
 */
public record StepShapeDimensionRepresentation(
    int id,
    String name,
    List<StepEntity> items,
    StepEntity context) implements StepEntity {

  public StepShapeDimensionRepresentation {
    items = List.copyOf(items);
  }
}
