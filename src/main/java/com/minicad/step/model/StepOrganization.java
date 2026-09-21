package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Minimal ORGANIZATION metadata.
 *
 * @param id STEP instance id
 * @param identifier organization identifier
 * @param name organization name
 * @param description organization description
 */
public final class StepOrganization extends AbstractStepEntity {
    private final String identifier;
    private final String description;

    public StepOrganization(int id, String identifier, String name, String description) {
        super(id, name);
        this.identifier = identifier;
        this.description = description;
    }

    public String getIdentifier() {
        return identifier;
    }

    public String getDescription() {
        return description;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("identifier", identifier);
        state.put("name", getName());
        state.put("description", description);
        return state;
    }
}
