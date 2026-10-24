package com.minicad.step.model;

import java.util.Map;

public final class StepKinematicStructure extends AbstractStepEntity {
    private final String description;
    private final StepEntity mechanism;

    public StepKinematicStructure(int id, String name, String description, StepEntity mechanism) {
        super(id, name);
        this.description = description;
        this.mechanism = mechanism;
    }

    public String getDescription() {
        return description;
    }

    public StepEntity getMechanism() {
        return mechanism;
    }

    @Override
    protected Map<String, Object> components() {
        return componentsOf(
                "id", getId(), "name", getName(), "description", description, "mechanism", mechanism);
    }
}
