package com.minicad.step.model;

import java.util.List;

/**
 * Resolved CIRCULAR_PATTERN.
 * Represents a circular pattern feature in manufacturing.
 *
 * @param id STEP instance id
 * @param name pattern name
 * @param baseFeature base feature being patterned
 * @param axis pattern axis
 * @param angularSpacing angular spacing between features
 * @param count number of features
 */
public record StepCircularPattern(
    int id,
    String name,
    StepEntity baseFeature,
    StepEntity axis,
    Double angularSpacing,
    Integer count) implements StepEntity {
}