package com.minicad.step.model.kinematic;

import com.minicad.step.model.base.StepEntity;
/**
 * Resolved CYLINDRICAL_JOINT.
 * A cylindrical joint between two links.
 */
public record StepCylindricalJoint(
    int id,
    String name,
    String description,
    StepEntity link1,
    StepEntity link2,
    StepEntity axis) implements StepEntity {
}
