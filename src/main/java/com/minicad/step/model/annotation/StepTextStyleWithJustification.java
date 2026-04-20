package com.minicad.step.model.annotation;

import com.minicad.step.model.base.StepEntity;
/**
 * Minimal TEXT_STYLE_WITH_JUSTIFICATION.
 *
 * @param id STEP instance id
 * @param name style name
 * @param characterAppearance character appearance definition
 * @param justification justification token
 */
public record StepTextStyleWithJustification(
        int id,
        String name,
        StepEntity characterAppearance,
        String justification
) implements StepEntity {
}
