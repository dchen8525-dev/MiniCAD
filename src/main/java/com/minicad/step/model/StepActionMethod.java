package com.minicad.step.model;

import java.util.Map;

public final class StepActionMethod extends AbstractStepEntity {
    private final String description;
    private final String method;

    public StepActionMethod(int id, String name, String description, String method) {
        super(id, name);
        this.description = description;
        this.method = method;
    }

    public String getDescription() {
        return description;
    }

    public String getMethod() {
        return method;
    }

    @Override
    protected Map<String, Object> components() {
        return componentsOf(
                "id", getId(), "name", getName(), "description", description, "method", method);
    }
}
