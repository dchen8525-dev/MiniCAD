package com.minicad.step.model.classification;

import com.minicad.step.model.base.StepEntity;
/**
 * Resolved SHAPE_ASPECT_SHAPE_REPRESENTATION.
 * Shape representation for shape aspects.
 */
public record StepShapeAspectShapeRepresentation(
    int id,
    String name,
    StepEntity ofShape) implements StepEntity {
}
