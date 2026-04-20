package com.minicad.step.model.annotation;

import com.minicad.step.model.base.StepEntity;
/**
 * Minimal CHARACTER_GLYPH_STYLE_OUTLINE_WITH_CHARACTERISTICS.
 *
 * @param id STEP instance id
 * @param outlineStyle referenced curve style
 * @param characteristics referenced fill area style
 */
public record StepCharacterGlyphStyleOutlineWithCharacteristics(
        int id,
        StepCurveStyle outlineStyle,
        StepFillAreaStyle characteristics
) implements StepEntity {

    @Override
    public String name() {
        return "";
    }
}
