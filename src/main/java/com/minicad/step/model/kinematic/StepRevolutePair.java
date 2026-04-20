package com.minicad.step.model.kinematic;

import com.minicad.step.model.base.StepEntity;
/**
 * Resolved REVOLUTE_PAIR.
 * A revolute (hinge) kinematic pair allowing rotation about one axis.
 */
public record StepRevolutePair(
    int id,
    String name,
    String description,
    StepEntity position,
    StepEntity axis,
    StepEntity link1,
    StepEntity link2
) implements StepEntity {}
