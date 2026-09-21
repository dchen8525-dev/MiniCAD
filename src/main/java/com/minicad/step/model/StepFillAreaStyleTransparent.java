package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Resolved FILL_AREA_STYLE_TRANSPARENT.
 */
public final class StepFillAreaStyleTransparent extends AbstractStepEntity {
    private final double transparency;

    public StepFillAreaStyleTransparent(int id, String name, double transparency) {
        super(id, name);
        this.transparency = transparency;
    }

    public double getTransparency() {
        return transparency;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("transparency", transparency);
        return state;
    }
}
