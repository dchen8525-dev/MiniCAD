package com.minicad.step.model.config_mgmt;

import com.minicad.step.model.base.StepEntity;

/**
 * Resolved DATE_EFFECTIVITY.
 */
public record StepDateEffectivity(
    int id,
    String name,
    StepEntity effectiveDate
) implements StepEntity {
}
