package com.minicad.step.model.product;

import com.minicad.step.model.base.StepEntity;
/**
 * Resolved SHAPE_REPRESENTATION_TRANSFORMATION.
 * A transformation between shape representations.
 */
public record StepShapeRepresentationTransformation(
    int id,
    String name,
    String transformationType,
    StepEntity transformation) implements StepEntity {
}
