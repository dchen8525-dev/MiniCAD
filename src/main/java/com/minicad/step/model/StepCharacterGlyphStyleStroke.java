package com.minicad.step.model;

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
