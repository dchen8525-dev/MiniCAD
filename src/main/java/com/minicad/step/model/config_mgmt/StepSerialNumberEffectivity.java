package com.minicad.step.model.config_mgmt;

import com.minicad.step.model.base.StepEntity;

/**
 * Resolved SERIAL_NUMBER_EFFECTIVITY.
 */
public record StepSerialNumberEffectivity(
    int id,
    String name,
    String serialNumber
) implements StepEntity {
}
