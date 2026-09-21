package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Minimal product definition shape.
 *
 * @param id STEP instance id
 * @param name shape name
 * @param description optional description
 * @param definition referenced product definition or product definition relationship (may be null for profile definitions)
 */
public final class StepProductDefinitionShape extends AbstractStepEntity {
    private final String description;
    private final StepEntity definition;  // May be null for profile definitions in complex entities

    public StepProductDefinitionShape(int id, String name, String description, StepEntity definition) {
        super(id, name);
        this.description = description;
        this.definition = definition;  // Accepts null for omitted parameter
    }

    public String getDescription() {
        return description;
    }

    public StepEntity getDefinition() {
        return definition;
    }

    // Record-style accessors
    public int id() { return getId(); }
    public String name() { return getName(); }
    public String description() { return description; }
    public StepEntity definition() { return definition; }

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
