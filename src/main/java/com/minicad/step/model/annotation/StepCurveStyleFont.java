package com.minicad.step.model.annotation;

import com.minicad.step.model.base.StepEntity;

/**
 * Resolved CURVE_STYLE_FONT.
 */
public record StepCurveStyleFont(
    int id,
    String name,
    StepEntity font
) implements StepEntity {
}
