package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Resolved CURVE_STYLE_FONT.
 */
public final class StepCurveStyleFont extends AbstractStepEntity {
    private final StepEntity font;

    public StepCurveStyleFont(int id, String name, StepEntity font) {
        super(id, name);
        this.font = font;
    }

    public StepEntity getFont() {
        return font;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("font", font);
        return state;
    }
}
