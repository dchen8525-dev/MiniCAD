package com.minicad.step.model.kinematic;

import com.minicad.step.model.base.StepEntity;
/**
 * Resolved PRISMATIC_JOINT.
 * A prismatic (translational) joint between two links.
 */
public record StepPrismaticJoint(
    int id,
    String name,
    String description,
    StepEntity link1,
    StepEntity link2,
    StepEntity axis) implements StepEntity {
}
