package com.minicad.step.model.classification;

import com.minicad.step.model.base.StepEntity;

/**
 * Resolved COMPOSITE_SHAPE_ASPECT.
 */
public record StepCompositeShapeAspect(
    int id,
    String name,
    String description,
    StepEntity ofShape,
    boolean productDefinitional) implements StepEntity {
}
