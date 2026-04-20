package com.minicad.step.model.annotation;

import com.minicad.step.model.base.StepEntity;

/**
 * Resolved CURVE_STYLE_RENDERING.
 */
public record StepCurveStyleRendering(
    int id,
    String name,
    double transparency,
    StepEntity colour
) implements StepEntity {
}
