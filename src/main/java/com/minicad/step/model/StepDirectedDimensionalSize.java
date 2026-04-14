package com.minicad.step.model;

/**
 * Resolved DIRECTED_DIMENSIONAL_SIZE.
 * A dimensional size with a direction for tolerance.
 *
 * @param id STEP instance id
 * @param name size name
 * @param magnitude size magnitude
 * @param direction reference direction for the measurement
 */
public record StepDirectedDimensionalSize(
    int id,
    String name,
    double magnitude,
    StepEntity direction) implements StepEntity {
}
