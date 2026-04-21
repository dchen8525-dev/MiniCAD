package com.minicad.step.model.annotation;

import com.minicad.step.model.base.StepEntity;
/**
 * Resolved FILL_AREA_SHAPE_USE.
 * A fill area shape use entity.
 */
public record StepFillAreaShapeUse(
    int id,
    String name,
    StepEntity fillArea) implements StepEntity {
}
