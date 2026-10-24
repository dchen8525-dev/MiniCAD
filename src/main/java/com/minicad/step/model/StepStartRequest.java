package com.minicad.step.model;

import java.util.Map;

/**
 * Resolved START_REQUEST.
 * A start request in AP203 configuration management.
 */
public final class StepStartRequest extends AbstractStepEntity {
    private final String description;

    public StepStartRequest(int id, String name, String description) {
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
