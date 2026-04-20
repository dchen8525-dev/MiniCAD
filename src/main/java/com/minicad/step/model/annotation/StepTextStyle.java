package com.minicad.step.model.annotation;

import com.minicad.step.model.base.StepEntity;
/**
 * Minimal TEXT_STYLE.
 *
 * @param id STEP instance id
 * @param name style name
 * @param characterAppearance character appearance definition
 */
public record StepTextStyle(
        int id,
        String name,
        StepEntity characterAppearance
) implements StepEntity {
}
