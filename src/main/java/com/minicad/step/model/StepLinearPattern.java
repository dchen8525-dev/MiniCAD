package com.minicad.step.model;

import java.util.List;

/**
 * Resolved LINEAR_PATTERN.
 * Represents a linear pattern feature in manufacturing.
 *
 * @param id STEP instance id
 * @param name pattern name
 * @param baseFeature base feature being patterned
 * @param direction pattern direction
 * @param spacing spacing between features
 * @param count number of features
 */
public record StepLinearPattern(
    int id,
    String name,
    StepEntity baseFeature,
    StepEntity direction,
    Double spacing,
    Integer count) implements StepEntity {
}