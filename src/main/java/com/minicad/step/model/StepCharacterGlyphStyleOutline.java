package com.minicad.step.model;

/**
 * Minimal CHARACTER_GLYPH_STYLE_OUTLINE.
 *
 * @param id STEP instance id
 * @param outlineStyle referenced curve style
 */
public record StepCharacterGlyphStyleOutline(
        int id,
        StepCurveStyle outlineStyle
) implements StepEntity {

    @Override
    public String name() {
        return "";
    }
}
