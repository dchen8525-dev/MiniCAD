package com.minicad.step.model.annotation;

import com.minicad.step.model.base.StepEntity;

/**
 * Resolved FILL_AREA_STYLE_TILING.
 */
public record StepFillAreaStyleTiling(
    int id,
    String name,
    StepEntity tilingPattern
) implements StepEntity {
}
