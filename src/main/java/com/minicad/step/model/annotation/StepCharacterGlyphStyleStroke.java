package com.minicad.step.model.annotation;

import com.minicad.step.model.base.StepEntity;
/**
 * Minimal CHARACTER_GLYPH_STYLE_STROKE.
 *
 * @param id STEP instance id
 * @param strokeStyle referenced curve style
 */
public record StepCharacterGlyphStyleStroke(
        int id,
        StepCurveStyle strokeStyle
) implements StepEntity {

    @Override
    public String name() {
        return "";
    }
}
