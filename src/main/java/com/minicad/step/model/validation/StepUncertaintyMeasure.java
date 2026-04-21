package com.minicad.step.model.validation;

import com.minicad.step.model.base.StepEntity;
/**
 * Resolved UNCERTAINTY_MEASURE.
 * An uncertainty measure with unit.
 */
public record StepUncertaintyMeasure(
    int id,
    String name,
    double value,
    String unit) implements StepEntity {
}
