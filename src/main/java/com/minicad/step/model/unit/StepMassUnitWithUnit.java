package com.minicad.step.model.unit;

import com.minicad.step.model.base.StepEntity;

/**
 * Resolved MASS_UNIT_WITH_UNIT.
 */
public record StepMassUnitWithUnit(
    int id,
    String name,
    StepEntity massUnit,
    StepEntity unitComponent
) implements StepEntity {
}
