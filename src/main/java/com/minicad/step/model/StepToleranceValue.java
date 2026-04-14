package com.minicad.step.model;

/**
 * Resolved TOLERANCE_VALUE.
 * A tolerance value specification.
 *
 * @param id STEP instance id
 * @param name tolerance name
 * @param lowerBound lower bound value
 * @param upperBound upper bound value
 */
public record StepToleranceValue(
    int id,
    String name,
    double lowerBound,
    double upperBound) implements StepEntity {
}
