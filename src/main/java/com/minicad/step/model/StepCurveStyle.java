package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Minimal CURVE_STYLE.
 *
 * @param id step id
 * @param name style name
 * @param curveFont referenced font
 * @param curveWidth stroke width
 * @param colour referenced colour
 */
public final class StepCurveStyle extends AbstractStepEntity {
    private final StepEntity curveFont;
    private final double curveWidth;
    private final StepEntity colour;

    public StepCurveStyle(int id, String name, StepEntity curveFont, double curveWidth, StepEntity colour) {
        super(id, name);
        this.curveFont = curveFont;
        this.curveWidth = curveWidth;
        this.colour = colour;
    }

    public StepEntity getCurveFont() {
        return curveFont;
    }

    public double getCurveWidth() {
        return curveWidth;
    }

    public StepEntity getColour() {
        return colour;
    }

    // Record-style accessors
    public StepEntity curveFont() {
        return curveFont;
    }

    public double curveWidth() {
        return curveWidth;
    }

    public StepEntity colour() {
        return colour;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("curveFont", curveFont);
        state.put("curveWidth", curveWidth);
        state.put("colour", colour);
        return state;
    }
}
