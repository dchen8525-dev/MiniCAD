package com.minicad.step.model.kinematic;

import com.minicad.step.model.base.StepEntity;
/**
 * Resolved SPHERICAL_JOINT.
 * A spherical (ball) joint between two links.
 */
public record StepSphericalJoint(
    int id,
    String name,
    String description,
    StepEntity link1,
    StepEntity link2,
    StepEntity center) implements StepEntity {
}
