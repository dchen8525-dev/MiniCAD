package com.minicad.step.model.kinematic;

import com.minicad.step.model.base.StepEntity;
/**
 * Resolved MOTION_CONSTRAINT.
 * A motion constraint for kinematic joints.
 */
public record StepMotionConstraint(
    int id,
    String name,
    String constraintType,
    double lowerLimit,
    double upperLimit) implements StepEntity {
}
