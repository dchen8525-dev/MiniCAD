package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Resolved SPECIFIED_HIGHER_USAGE_OCCURRENCE.
 * Higher-level product usage occurrence.
 */
public final class StepSpecifiedHigherUsageOccurrence extends AbstractStepEntity {
    private final String description;
    private final StepEntity usage;

    public StepSpecifiedHigherUsageOccurrence(int id, String name, String description, StepEntity usage) {
        super(id, name);
        this.description = description;
        this.usage = usage;
    }

    public String getDescription() {
        return description;
    }

    public StepEntity getUsage() {
        return usage;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("description", description);
        state.put("usage", usage);
        return state;
    }
}
