package com.minicad.step.model.config_mgmt;

import com.minicad.step.model.base.StepEntity;

/**
 * Resolved LOT_EFFECTIVITY.
 */
public record StepLotEffectivity(
    int id,
    String name,
    StepEntity effectivityLot
) implements StepEntity {
}
