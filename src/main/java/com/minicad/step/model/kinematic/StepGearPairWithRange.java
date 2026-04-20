package com.minicad.step.model.kinematic;

import com.minicad.step.model.base.StepEntity;
/**
 * Resolved GEAR_PAIR_WITH_RANGE.
 * A gear pair with specified range limits.
 */
public record StepGearPairWithRange(
    int id,
    String name,
    String description,
    StepEntity gear1,
    StepEntity gear2,
    Double ratio,
    Double lowerRange,
    Double upperRange,
    StepEntity link1,
    StepEntity link2
) implements StepEntity {}
