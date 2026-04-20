package com.minicad.step.model.annotation;

import com.minicad.step.model.base.StepEntity;

/**
 * Resolved CHARACTER_GLYPH_OUTLINE.
 */
public record StepCharacterGlyphOutline(
    int id,
    String name,
    StepEntity glyph,
    StepEntity outline
) implements StepEntity {
}
