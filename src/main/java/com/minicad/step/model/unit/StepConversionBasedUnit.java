package com.minicad.step.model.unit;

import com.minicad.step.model.base.StepEntity;
/**
 * Minimal conversion-based unit definition.
 *
 * @param id STEP instance id
 * @param name unit label
 * @param unitKind derived unit kind such as LENGTH_UNIT
 * @param conversionFactor referenced conversion factor
 */
public record StepConversionBasedUnit(
    int id,
    String name,
    String unitKind,
    StepMeasureWithUnit conversionFactor) implements StepEntity {
}
