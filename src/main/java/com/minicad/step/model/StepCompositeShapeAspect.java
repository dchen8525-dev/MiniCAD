package com.minicad.step.model;

import java.util.List;

/**
 * Resolved COMPOSITE_SHAPE_ASPECT.
 * A composite of multiple shape aspects forming a combined feature.
 *
 * @param id STEP instance id
 * @param name aspect name
 * @param description aspect description
 * @param ofShape product definition shape
 * @param componentRelationships component relationships
 */
public record StepCompositeShapeAspect(
    int id,
    String name,
    String description,
    StepEntity ofShape,
    List<StepEntity> componentRelationships) implements StepEntity {

  public StepCompositeShapeAspect {
    componentRelationships = List.copyOf(componentRelationships);
  }
}
