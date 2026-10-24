package com.minicad.step.model;

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
        return componentsOf(
                "id", getId(), "name", getName(), "description", description, "directive", directive);
    }
}
