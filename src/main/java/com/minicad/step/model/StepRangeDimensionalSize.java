package com.minicad.step.model;

/**
 * Resolved RANGE_DIMENSIONAL_SIZE.
 * Dimensional size with range bounds.
 */
public record StepRangeDimensionalSize(
    int id,
    String name,
    String description,
    double lowerBound,
    double upperBound) implements StepEntity {
}
