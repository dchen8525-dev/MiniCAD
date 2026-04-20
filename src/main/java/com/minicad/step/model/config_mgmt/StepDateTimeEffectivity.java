package com.minicad.step.model.config_mgmt;

import com.minicad.step.model.base.StepEntity;

/**
 * Resolved DATE_TIME_EFFECTIVITY.
 */
public record StepDateTimeEffectivity(
    int id,
    String name,
    StepEntity effectiveDateTime
) implements StepEntity {
}
