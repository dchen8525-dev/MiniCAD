package com.minicad.step.model.annotation;

import com.minicad.step.model.base.StepEntity;

/**
 * Resolved CHARACTER_GLYPH.
 */
public record StepCharacterGlyph(
    int id,
    String name,
    String characterCode
) implements StepEntity {
}
