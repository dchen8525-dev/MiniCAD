package com.minicad.step.model.kinematic;

import com.minicad.step.model.base.StepEntity;
/**
 * Resolved KINEMATIC_CHAIN.
 * A chain of kinematic links and joints.
 */
public record StepKinematicChain(
    int id,
    String name,
    String description) implements StepEntity {
}
