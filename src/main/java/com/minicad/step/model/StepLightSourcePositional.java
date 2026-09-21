package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.Map;

public final class StepLightSourcePositional extends AbstractStepEntity {
    private final StepEntity color;
    private final double intensity;
    private final StepEntity position;

    public StepLightSourcePositional(int id, String name, StepEntity color, double intensity, StepEntity position) {
        super(id, name);
        this.color = color;
        this.intensity = intensity;
        this.position = position;
    }

    public StepEntity getColor() {
        return color;
    }

    public double getIntensity() {
        return intensity;
    }

    public StepEntity getPosition() {
        return position;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("color", color);
        state.put("intensity", intensity);
        state.put("position", position);
        return state;
    }
}
