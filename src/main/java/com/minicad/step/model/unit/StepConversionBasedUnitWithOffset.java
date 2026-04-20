package com.minicad.step.model.unit;

import com.minicad.step.model.base.StepEntity;
/**
 * Minimal conversion-based unit with offset definition.
 *
 * @param id STEP instance id
 * @param name unit label
 * @param unitKind derived unit kind such as THERMODYNAMIC_TEMPERATURE_UNIT
 * @param conversionFactor referenced conversion factor
 * @param conversionOffset scalar offset
 */
public record StepConversionBasedUnitWithOffset(
    int id,
    String name,
    String unitKind,
    StepMeasureWithUnit conversionFactor,
    double conversionOffset) implements StepEntity {
}
