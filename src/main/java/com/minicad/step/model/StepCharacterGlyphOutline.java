package com.minicad.step.model;

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
        return componentsOf(
                "id", getId(), "name", getName(), "glyph", glyph, "outline", outline);
    }
}
