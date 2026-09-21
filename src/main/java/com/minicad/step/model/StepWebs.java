package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Resolved WEBS.
 */
public final class StepWebs extends AbstractStepEntity {
    private final double thickness;

    public StepWebs(int id, String name, double thickness) {
        super(id, name);
        this.thickness = thickness;
    }

    public double getThickness() {
        return thickness;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("thickness", thickness);
        return state;
    }
}
