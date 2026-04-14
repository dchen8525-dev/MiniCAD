package com.minicad.step.model;

/**
 * Resolved PLUS_MINUS_TOLERANCE.
 * A plus/minus tolerance specification.
 *
 * @param id STEP instance id
 * @param name tolerance name
 * @param range tolerance range
 * @param tolerancedMeasure toleranced measure
 */
public record StepPlusMinusTolerance(
    int id,
    String name,
    StepEntity range,
    StepEntity tolerancedMeasure) implements StepEntity {
}
