package com.minicad.step.model;

/**
 * Minimal TEXT_STYLE_WITH_MIRROR.
 *
 * @param id STEP instance id
 * @param name style name
 * @param characterAppearance character appearance definition
 * @param mirrorPlacement mirror axis placement
 */
public record StepTextStyleWithMirror(
        int id,
        String name,
        StepEntity characterAppearance,
        StepEntity mirrorPlacement
) implements StepEntity {
}
