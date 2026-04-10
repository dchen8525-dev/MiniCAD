package com.minicad.step.model;

import java.util.List;

/**
 * Minimal TEXT_STYLE_WITH_BOX_CHARACTERISTICS.
 *
 * @param id STEP instance id
 * @param name style name
 * @param characterAppearance character appearance definition
 * @param boxCharacteristics raw box characteristic literals
 */
public record StepTextStyleWithBoxCharacteristics(
        int id,
        String name,
        StepEntity characterAppearance,
        List<String> boxCharacteristics
) implements StepEntity {

    public StepTextStyleWithBoxCharacteristics {
        boxCharacteristics = List.copyOf(boxCharacteristics);
    }
}
