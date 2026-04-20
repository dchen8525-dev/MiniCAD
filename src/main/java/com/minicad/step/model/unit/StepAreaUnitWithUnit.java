package com.minicad.step.model.unit;

import com.minicad.step.model.base.StepEntity;

/**
 * Resolved AREA_UNIT_WITH_UNIT.
 */
public record StepAreaUnitWithUnit(
    int id,
    String name,
    StepEntity areaUnit,
    StepEntity unitComponent
) implements StepEntity {
}
