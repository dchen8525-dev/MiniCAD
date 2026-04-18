package com.minicad.step.model;

import java.util.List;

/**
 * Resolved LOW_ORDER_KINEMATIC_PAIR_WITH_RANGE.
 * A low-order kinematic pair with specified range limits.
 */
public record StepLowOrderKinematicPairWithRange(
    int id,
    String name,
    String description,
    StepEntity position,
    StepEntity direction,
    Double lowerRange,
    Double upperRange,
    StepEntity link1,
    StepEntity link2
) implements StepEntity {
}
