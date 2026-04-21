package com.minicad.step.model.kinematic;

import com.minicad.step.model.base.StepEntity;
/**
 * Resolved PLANAR_JOINT.
 * A planar joint between two links.
 */
public record StepPlanarJoint(
    int id,
    String name,
    String description,
    StepEntity link1,
    StepEntity link2,
    StepEntity plane) implements StepEntity {
}
