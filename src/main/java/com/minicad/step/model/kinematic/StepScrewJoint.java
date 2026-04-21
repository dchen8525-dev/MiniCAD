package com.minicad.step.model.kinematic;

import com.minicad.step.model.base.StepEntity;
/**
 * Resolved SCREW_JOINT.
 * A screw joint between two links.
 */
public record StepScrewJoint(
    int id,
    String name,
    String description,
    StepEntity link1,
    StepEntity link2,
    double pitch) implements StepEntity {
}
