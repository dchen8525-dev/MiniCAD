package com.minicad.step.model.kinematic;

import com.minicad.step.model.base.StepEntity;
/**
 * Resolved DIRECTION_SENSE.
 * Direction sense for kinematic joints.
 */
public record StepDirectionSense(
    int id,
    String name,
    String sense) implements StepEntity {
}
