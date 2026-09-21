package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.Map;

public final class StepLightSourceDirectional extends AbstractStepEntity {
    private final StepEntity color;
    private final double intensity;
    private final StepEntity orientation;

    public StepLightSourceDirectional(int id, String name, StepEntity color, double intensity, StepEntity orientation) {
        super(id, name);
        this.color = color;
        this.intensity = intensity;
        this.orientation = orientation;
    }

    public StepEntity getColor() {
        return color;
    }

    public double getIntensity() {
        return intensity;
    }

    public StepEntity getOrientation() {
        return orientation;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("color", color);
        state.put("intensity", intensity);
        state.put("orientation", orientation);
        return state;
    }
}
