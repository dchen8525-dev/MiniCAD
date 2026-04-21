package com.minicad.step.model.kinematic;

import com.minicad.step.model.base.StepEntity;
/**
 * Resolved REVOLUTE_JOINT.
 * A revolute (rotational) joint between two links.
 */
public record StepRevoluteJoint(
    int id,
    String name,
    String description,
    StepEntity link1,
    StepEntity link2,
    StepEntity axis) implements StepEntity {
}
