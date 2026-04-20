package com.minicad.step.model.unit;

import com.minicad.step.model.base.StepEntity;

/**
 * Resolved CONVERSION_BASED_UNIT_AND_UNIT.
 */
public record StepConversionBasedUnitAndUnit(
    int id,
    String name,
    StepEntity convertedUnit,
    StepEntity unitComponent
) implements StepEntity {
}
