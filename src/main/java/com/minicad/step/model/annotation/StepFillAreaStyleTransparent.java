package com.minicad.step.model.annotation;

import com.minicad.step.model.base.StepEntity;

/**
 * Resolved FILL_AREA_STYLE_TRANSPARENT.
 */
public record StepFillAreaStyleTransparent(
    int id,
    String name,
    double transparency
) implements StepEntity {
}
