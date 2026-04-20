package com.minicad.step.model.kinematic;

import com.minicad.step.model.base.StepEntity;
/**
 * Resolved UNIVERSAL_PAIR.
 * A universal (Hooke's joint) kinematic pair allowing rotation about two intersecting axes.
 */
public record StepUniversalPair(
    int id,
    String name,
    String description,
    StepEntity position,
    StepEntity axis1,
    StepEntity axis2,
    StepEntity link1,
    StepEntity link2
) implements StepEntity {}
