package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Minimal CHARACTER_GLYPH_STYLE_OUTLINE.
 *
 * @param id STEP instance id
 * @param outlineStyle referenced curve style
 */
public final class StepCharacterGlyphStyleOutline extends AbstractStepEntity {
    private final StepCurveStyle outlineStyle;

    public StepCharacterGlyphStyleOutline(int id, StepCurveStyle outlineStyle) {
        super(id, "");
        this.outlineStyle = outlineStyle;
    }

    public StepCurveStyle getOutlineStyle() {
        return outlineStyle;
    }

    // Record-style accessor - no name field, return empty string
    public String name() {
        return "";
    }

    public StepCurveStyle outlineStyle() {
        return outlineStyle;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("outlineStyle", outlineStyle);
        return state;
    }
}
