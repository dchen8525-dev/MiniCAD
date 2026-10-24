package com.minicad.step.model;

import java.util.Map;

public final class StepActionStatus extends AbstractStepEntity {
    private final String description;

    public StepActionStatus(int id, String name, String description) {
        super(id, name);
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    @Override
    protected Map<String, Object> components() {
        return componentsOf(
                "id", getId(), "name", getName(), "description", description);
    }
}
