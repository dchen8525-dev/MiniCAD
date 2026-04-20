package com.minicad.step.model.annotation;

import com.minicad.step.model.base.StepEntity;
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
