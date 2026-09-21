package com.minicad.step.model;

import java.util.LinkedHashMap;
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
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("description", description);
        state.put("method", method);
        return state;
    }
}
