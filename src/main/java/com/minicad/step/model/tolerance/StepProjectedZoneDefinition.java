package com.minicad.step.model.tolerance;

import com.minicad.step.model.base.StepEntity;

/**
 * Resolved PROJECTED_ZONE_DEFINITION.
 * A projected tolerance zone definition entity.
 *
 * @param id STEP instance id
 * @param name zone name
 * @param description zone description
 * @param projectedZone the projected zone entity reference
 * @param applied whether the projected zone is applied
 */
public record StepProjectedZoneDefinition(
        int id,
        String name,
        String description,
        StepEntity projectedZone,
        boolean applied) implements StepEntity {
}
