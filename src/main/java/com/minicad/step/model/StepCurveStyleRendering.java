package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Resolved CURVE_STYLE_RENDERING.
 */
public final class StepCurveStyleRendering extends AbstractStepEntity {
    private final double transparency;
    private final StepEntity colour;

    public StepCurveStyleRendering(int id, String name, double transparency, StepEntity colour) {
        super(id, name);
        this.transparency = transparency;
        this.colour = colour;
    }

    public double getTransparency() {
        return transparency;
    }

    public StepEntity getColour() {
        return colour;
    }

    // Record-style accessors
    public String name() {
        return getName();
    }

    public double transparency() {
        return transparency;
    }

    public StepEntity colour() {
        return colour;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("transparency", transparency);
        state.put("colour", colour);
        return state;
    }
}
