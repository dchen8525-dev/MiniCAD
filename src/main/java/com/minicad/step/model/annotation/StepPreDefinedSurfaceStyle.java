package com.minicad.step.model.annotation;

import com.minicad.step.model.base.StepEntity;

/**
 * Resolved PRE_DEFINED_SURFACE_STYLE.
 */
public record StepPreDefinedSurfaceStyle(
    int id,
    String name,
    String identifier
) implements StepEntity {
}
