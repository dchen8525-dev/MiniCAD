package com.minicad.step.model.kinematic;

import com.minicad.step.model.base.StepEntity;
/**
 * Resolved ACTUATED_KINEMATIC_PAIR.
 * A kinematic pair with an actuator providing driven motion.
 */
public record StepActuatedKinematicPair(
    int id,
    String name,
    String description,
    StepEntity basePair,
    StepEntity actuator,
    Double actuationSpeed
) implements StepEntity {}
