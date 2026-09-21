package com.minicad.step.model;

import java.util.LinkedHashMap;
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
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("description", description);
        state.put("angle", angle);
        return state;
    }
}
