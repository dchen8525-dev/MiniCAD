package com.minicad.step.model.unit;

import com.minicad.step.model.base.StepEntity;

/**
 * Resolved LENGTH_UNIT_WITH_UNIT.
 */
public record StepLengthUnitWithUnit(
    int id,
    String name,
    StepEntity lengthUnit,
    StepEntity unitComponent
) implements StepEntity {
}
