package com.minicad.step.model;

import java.util.Map;

public final class StepLightSource extends AbstractStepEntity {
    private final StepEntity color;
    private final double intensity;

    public StepLightSource(int id, String name, StepEntity color, double intensity) {
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
        return componentsOf(
                "id", getId(), "name", getName(), "color", color, "intensity", intensity);
    }
}
