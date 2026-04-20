package com.minicad.step.model.unit;

import com.minicad.step.model.base.StepEntity;

/**
 * Resolved PLANE_ANGLE_UNIT_WITH_UNIT.
 */
public record StepPlaneAngleUnitWithUnit(
    int id,
    String name,
    StepEntity angleUnit,
    StepEntity unitComponent
) implements StepEntity {
}
