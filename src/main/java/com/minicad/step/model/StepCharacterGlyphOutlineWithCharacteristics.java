package com.minicad.step.model;

import java.util.Map;

/**
 * Resolved CHARACTER_GLYPH_OUTLINE_WITH_CHARACTERISTICS.
 */
public final class StepCharacterGlyphOutlineWithCharacteristics extends AbstractStepEntity {
    private final StepEntity glyph;
    private final StepEntity outline;
    private final StepEntity characteristics;

    public StepCharacterGlyphOutlineWithCharacteristics(int id, String name, StepEntity glyph, StepEntity outline, StepEntity characteristics) {
        super(id, name);
        this.glyph = glyph;
        this.outline = outline;
        this.characteristics = characteristics;
    }

    public StepEntity getGlyph() {
        return glyph;
    }

    public StepEntity getOutline() {
        return outline;
    }

    public StepEntity getCharacteristics() {
        return characteristics;
    }

    @Override
    protected Map<String, Object> components() {
        return componentsOf(
                "id", getId(), "name", getName(), "glyph", glyph, "outline", outline,
                "characteristics", characteristics);
    }
}
