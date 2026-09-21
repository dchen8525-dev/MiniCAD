package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.Map;

public final class StepLightSourceAmbient extends AbstractStepEntity {
    private final StepEntity color;
    private final double intensity;

    public StepLightSourceAmbient(int id, String name, StepEntity color, double intensity) {
        super(id, name);
        this.color = color;
        this.intensity = intensity;
    }

    public StepEntity getColor() {
        return color;
    }

    public double getIntensity() {
        return intensity;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("color", color);
        state.put("intensity", intensity);
        return state;
    }
}
