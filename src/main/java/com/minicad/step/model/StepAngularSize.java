package com.minicad.step.model;

import java.util.Map;

public final class StepAngularSize extends AbstractStepEntity {
    private final String description;
    private final double angle;

    public StepAngularSize(int id, String name, String description, double angle) {
        super(id, name);
        this.description = description;
        this.angle = angle;
    }

    public String getDescription() {
        return description;
    }

    public double getAngle() {
        return angle;
    }

    @Override
    protected Map<String, Object> components() {
        return componentsOf(
                "id", getId(), "name", getName(), "description", description, "angle", angle);
    }
}
