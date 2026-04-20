package com.minicad.step.model.annotation;

import com.minicad.step.model.base.StepEntity;

/**
 * Resolved CHARACTER_GLYPH_OUTLINE_WITH_CHARACTERISTICS.
 */
public record StepCharacterGlyphOutlineWithCharacteristics(
    int id,
    String name,
    StepEntity glyph,
    StepEntity outline,
    StepEntity characteristics
) implements StepEntity {
}
