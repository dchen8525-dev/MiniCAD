package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Resolved CHARACTER_GLYPH_OUTLINE.
 */
public final class StepCharacterGlyphOutline extends AbstractStepEntity {
    private final StepEntity glyph;
    private final StepEntity outline;

    public StepCharacterGlyphOutline(int id, String name, StepEntity glyph, StepEntity outline) {
        super(id, name);
        this.glyph = glyph;
        this.outline = outline;
    }

    public StepEntity getGlyph() {
        return glyph;
    }

    public StepEntity getOutline() {
        return outline;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("glyph", glyph);
        state.put("outline", outline);
        return state;
    }
}
