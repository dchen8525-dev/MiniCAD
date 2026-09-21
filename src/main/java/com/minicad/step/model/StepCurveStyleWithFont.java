package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Resolved CURVE_STYLE_WITH_FONT.
 */
public final class StepCurveStyleWithFont extends AbstractStepEntity {
    private final StepEntity font;
    private final double width;

    public StepCurveStyleWithFont(int id, String name, StepEntity font, double width) {
        super(id, name);
        this.font = font;
        this.width = width;
    }

    public StepEntity getFont() {
        return font;
    }

    public double getWidth() {
        return width;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("font", font);
        state.put("width", width);
        return state;
    }
}
