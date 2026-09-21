package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Minimal CHARACTER_GLYPH_STYLE_STROKE.
 *
 * @param id STEP instance id
 * @param strokeStyle referenced curve style
 */
public final class StepCharacterGlyphStyleStroke extends AbstractStepEntity {
    private final StepCurveStyle strokeStyle;

    public StepCharacterGlyphStyleStroke(int id, StepCurveStyle strokeStyle) {
        super(id, "");
        this.strokeStyle = strokeStyle;
    }

    public StepCurveStyle getStrokeStyle() {
        return strokeStyle;
    }

    // Record-style accessor
    public StepCurveStyle strokeStyle() {
        return strokeStyle;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("strokeStyle", strokeStyle);
        return state;
    }
}
