package com.minicad.step.model;

import java.util.LinkedHashMap;
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
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("glyph", glyph);
        state.put("outline", outline);
        state.put("characteristics", characteristics);
        return state;
    }
}
