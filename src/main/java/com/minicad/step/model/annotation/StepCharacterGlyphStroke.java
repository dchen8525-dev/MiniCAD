package com.minicad.step.model.annotation;

import com.minicad.step.model.base.StepEntity;

/**
 * Resolved CHARACTER_GLYPH_STROKE.
 */
public record StepCharacterGlyphStroke(
    int id,
    String name,
    StepEntity glyph,
    StepEntity stroke
) implements StepEntity {
}
