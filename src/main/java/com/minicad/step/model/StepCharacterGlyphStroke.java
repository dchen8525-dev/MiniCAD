package com.minicad.step.model;

import java.util.LinkedHashMap;
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
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("glyph", glyph);
        state.put("stroke", stroke);
        return state;
    }
}
