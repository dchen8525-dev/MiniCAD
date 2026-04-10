package com.minicad.step.model;

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
