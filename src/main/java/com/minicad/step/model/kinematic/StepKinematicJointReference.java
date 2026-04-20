package com.minicad.step.model.kinematic;

import com.minicad.step.model.base.StepEntity;

/**
 * Resolved KINEMATIC_JOINT_REFERENCE.
 */
public record StepKinematicJointReference(
    int id,
    String name,
    StepEntity joint
) implements StepEntity {
}
