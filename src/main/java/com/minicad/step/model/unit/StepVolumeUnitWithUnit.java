package com.minicad.step.model.unit;

import com.minicad.step.model.base.StepEntity;

/**
 * Resolved VOLUME_UNIT_WITH_UNIT.
 */
public record StepVolumeUnitWithUnit(
    int id,
    String name,
    StepEntity volumeUnit,
    StepEntity unitComponent
) implements StepEntity {
}
