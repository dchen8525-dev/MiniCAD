package com.minicad.step.model.tolerance;

import com.minicad.step.model.base.StepEntity;
/**
 * Resolved DIMENSIONAL_LOCATION_WITH_PATH.
 * A dimensional location that includes a path definition for the measurement route.
 */
public record StepDimensionalLocationWithPath(
    int id,
    String name,
    String description,
    StepEntity toleratedShape,
    StepEntity path) implements StepEntity {
}
