package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.Map;

public final class StepKinematicLink extends AbstractStepEntity {
    private final String description;
    private final StepEntity shape;

    public StepKinematicLink(int id, String name, String description, StepEntity shape) {
        super(id, name);
        this.description = description;
        this.shape = shape;
    }

    public String getDescription() {
        return description;
    }

    public StepEntity getShape() {
        return shape;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("description", description);
        state.put("shape", shape);
        return state;
    }
}
