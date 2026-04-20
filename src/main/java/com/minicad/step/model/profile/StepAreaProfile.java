package com.minicad.step.model.profile;

import com.minicad.step.model.base.StepEntity;

/**
 * Resolved AREA_PROFILE.
 */
public record StepAreaProfile(
    int id,
    String name,
    StepEntity profileDef
) implements StepEntity {
}
