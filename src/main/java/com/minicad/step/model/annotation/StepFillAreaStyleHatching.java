package com.minicad.step.model.annotation;

import com.minicad.step.model.base.StepEntity;

/**
 * Resolved FILL_AREA_STYLE_HATCHING.
 */
public record StepFillAreaStyleHatching(
    int id,
    String name,
    double angle,
    double spacing
) implements StepEntity {
}
