package com.minicad.step.model.kinematic;

import com.minicad.step.model.base.StepEntity;
/**
 * Resolved GEAR_PAIR.
 * A gear kinematic pair linking two revolute pairs with a gear ratio.
 */
public record StepGearPair(
    int id,
    String name,
    String description,
    StepEntity gear1,
    StepEntity gear2,
    Double ratio,
    StepEntity link1,
    StepEntity link2
) implements StepEntity {}
