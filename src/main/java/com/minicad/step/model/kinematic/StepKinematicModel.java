package com.minicad.step.model.kinematic;

import com.minicad.step.model.base.StepEntity;
/**
 * Resolved KINEMATIC_MODEL.
 * A kinematic model containing links and joints.
 */
public record StepKinematicModel(
    int id,
    String name,
    String description) implements StepEntity {
}
