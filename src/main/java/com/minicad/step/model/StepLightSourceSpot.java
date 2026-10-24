package com.minicad.step.model;

import java.util.Map;

public final class StepLightSourceSpot extends AbstractStepEntity {
    private final StepEntity color;
    private final double intensity;
    private final StepEntity position;
    private final StepEntity orientation;
    private final double concentration;
    private final double spreadAngle;

    public StepLightSourceSpot(int id, String name, StepEntity color, double intensity, StepEntity position, StepEntity orientation, double concentration, double spreadAngle) {
        super(id, name);
        this.color = color;
        this.intensity = intensity;
        this.position = position;
        this.orientation = orientation;
        this.concentration = concentration;
        this.spreadAngle = spreadAngle;
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

    public StepEntity getOrientation() {
        return orientation;
    }

    public double getConcentration() {
        return concentration;
    }

    public double getSpreadAngle() {
        return spreadAngle;
    }

    @Override
    protected Map<String, Object> components() {
        return componentsOf(
                "id", getId(), "name", getName(), "color", color, "intensity", intensity,
                "position", position, "orientation", orientation, "concentration", concentration,
                "spreadAngle", spreadAngle);
    }
}
