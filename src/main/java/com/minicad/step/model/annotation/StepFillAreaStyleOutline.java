package com.minicad.step.model.annotation;

import com.minicad.step.model.base.StepEntity;

/**
 * Resolved FILL_AREA_STYLE_OUTLINE.
 */
public record StepFillAreaStyleOutline(
    int id,
    String name,
    StepEntity style
) implements StepEntity {
}
