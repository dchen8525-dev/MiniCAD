package com.minicad.step.model.annotation;

import com.minicad.step.model.base.StepEntity;

/**
 * Resolved TEXT_FONT.
 */
public record StepTextFont(
    int id,
    String name,
    String fontName
) implements StepEntity {
}
