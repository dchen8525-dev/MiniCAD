package com.minicad.step.model;

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
        return componentsOf(
                "id", getId(), "name", getName(), "description", description, "shape", shape);
    }
}
