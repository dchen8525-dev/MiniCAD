package com.minicad.step.model;

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
