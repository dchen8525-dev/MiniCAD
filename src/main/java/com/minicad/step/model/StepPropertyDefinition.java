package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Minimal property definition metadata.
 *
 * @param id STEP instance id
 * @param name property name
 * @param description property description
 * @param definition related semantic target
 */
public final class StepPropertyDefinition extends AbstractStepEntity {
    private final String description;
    private final StepEntity definition;

    public StepPropertyDefinition(int id, String name, String description, StepEntity definition) {
        super(id, name);
        this.description = description;
        this.definition = definition;
    }

    public String getDescription() {
        return description;
    }

    public StepEntity getDefinition() {
        return definition;
    }

    // Record-style accessor
    public StepEntity definition() {
        return definition;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("description", description);
        state.put("definition", definition);
        return state;
    }
}
