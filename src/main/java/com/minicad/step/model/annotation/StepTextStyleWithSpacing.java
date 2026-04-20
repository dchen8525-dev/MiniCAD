package com.minicad.step.model.annotation;

import com.minicad.step.model.base.StepEntity;
/**
 * Minimal TEXT_STYLE_WITH_SPACING.
 *
 * @param id STEP instance id
 * @param name style name
 * @param characterAppearance character appearance definition
 * @param characterSpacing additional spacing between characters
 */
public record StepTextStyleWithSpacing(
        int id,
        String name,
        StepEntity characterAppearance,
        double characterSpacing
) implements StepEntity {
}
