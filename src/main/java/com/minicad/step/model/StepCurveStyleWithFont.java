package com.minicad.step.model;

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
        return componentsOf(
                "id", getId(), "name", getName(), "font", font, "width", width);
    }
}
