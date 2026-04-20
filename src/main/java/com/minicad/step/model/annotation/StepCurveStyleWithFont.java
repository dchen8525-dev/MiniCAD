package com.minicad.step.model.annotation;

import com.minicad.step.model.base.StepEntity;

/**
 * Resolved CURVE_STYLE_WITH_FONT.
 */
public record StepCurveStyleWithFont(
    int id,
    String name,
    StepEntity font,
    double width
) implements StepEntity {
}
