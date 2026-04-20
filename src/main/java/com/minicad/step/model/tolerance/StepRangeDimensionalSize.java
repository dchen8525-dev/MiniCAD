package com.minicad.step.model.tolerance;

import com.minicad.step.model.base.StepEntity;
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
