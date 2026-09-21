package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.Map;

public final class StepAction extends AbstractStepEntity {
    private final String description;
    private final String actionMethod;

    public StepAction(int id, String name, String description, String actionMethod) {
        super(id, name);
        this.description = description;
        this.actionMethod = actionMethod;
    }

    public String getDescription() {
        return description;
    }

    public String getActionMethod() {
        return actionMethod;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("description", description);
        state.put("actionMethod", actionMethod);
        return state;
    }
}
