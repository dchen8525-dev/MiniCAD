package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.Map;

public final class StepActionDirective extends AbstractStepEntity {
    private final String description;
    private final String directive;

    public StepActionDirective(int id, String name, String description, String directive) {
        super(id, name);
        this.description = description;
        this.directive = directive;
    }

    public String getDescription() {
        return description;
    }

    public String getDirective() {
        return directive;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("description", description);
        state.put("directive", directive);
        return state;
    }
}
