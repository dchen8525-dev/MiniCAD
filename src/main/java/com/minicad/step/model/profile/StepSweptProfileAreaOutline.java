package com.minicad.step.model.profile;

import com.minicad.step.model.base.StepEntity;

/**
 * Resolved SWEPT_PROFILE_AREA_OUTLINE.
 */
public record StepSweptProfileAreaOutline(
    int id,
    String name,
    StepEntity profileDef
) implements StepEntity {
}
