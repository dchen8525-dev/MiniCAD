package com.minicad.step.model.annotation;

import com.minicad.step.model.base.StepEntity;

/**
 * Resolved SURFACE_STYLE_PARAMETER_LINES.
 */
public record StepSurfaceStyleParameterLines(
    int id,
    String name,
    StepEntity surfaceStyle
) implements StepEntity {
}
