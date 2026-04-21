package com.minicad.step.model.kinematic;

import com.minicad.step.model.base.StepEntity;
/**
 * Resolved KINEMATIC_PROPERTY.
 * A kinematic property definition.
 */
public record StepKinematicProperty(
    int id,
    String name,
    String propertyType,
    StepEntity value) implements StepEntity {
}
