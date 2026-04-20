package com.minicad.step.model.profile;

import com.minicad.step.model.base.StepEntity;

/**
 * Resolved GENERALIZED_AREA_PROFILE.
 */
public record StepGeneralizedAreaProfile(
    int id,
    String name,
    StepEntity profileDef
) implements StepEntity {
}
