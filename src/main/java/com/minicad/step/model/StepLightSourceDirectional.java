package com.minicad.step.model;

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
        return componentsOf(
                "id", getId(), "name", getName(), "color", color, "intensity", intensity,
                "orientation", orientation);
    }
}
