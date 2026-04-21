package com.minicad.step.model.kinematic;

import com.minicad.step.model.base.StepEntity;
/**
 * Resolved JOINT_VALUE.
 * Joint value for kinematic joints.
 */
public record StepJointValue(
    int id,
    String name,
    double value,
    String unit) implements StepEntity {
}
