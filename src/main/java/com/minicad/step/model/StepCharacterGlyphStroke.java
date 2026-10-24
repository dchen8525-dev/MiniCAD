package com.minicad.step.model;

import java.util.Map;

/**
 * Resolved CHARACTER_GLYPH_STROKE.
 */
public final class StepCharacterGlyphStroke extends AbstractStepEntity {
    private final StepEntity glyph;
    private final StepEntity stroke;

    public StepCharacterGlyphStroke(int id, String name, StepEntity glyph, StepEntity stroke) {
        super(id, name);
        this.glyph = glyph;
        this.stroke = stroke;
    }

    public StepEntity getGlyph() {
        return glyph;
    }

    public StepEntity getStroke() {
        return stroke;
    }

    @Override
    protected Map<String, Object> components() {
        return componentsOf(
                "id", getId(), "name", getName(), "glyph", glyph, "stroke", stroke);
    }
}
