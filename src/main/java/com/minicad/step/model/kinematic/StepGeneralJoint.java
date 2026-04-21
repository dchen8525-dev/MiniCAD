package com.minicad.step.model.kinematic;

import com.minicad.step.model.base.StepEntity;
/**
 * Resolved GENERAL_JOINT.
 * A general joint between two links.
 */
public record StepGeneralJoint(
    int id,
    String name,
    String description,
    StepEntity link1,
    StepEntity link2) implements StepEntity {
}
