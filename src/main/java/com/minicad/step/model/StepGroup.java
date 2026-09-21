package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Minimal GROUP metadata.
 *
 * @param id STEP instance id
 * @param name group name
 * @param description group description
 * @param entityName concrete STEP entity name
 */
public final class StepGroup extends AbstractStepEntity {
    private final String description;
    private final String entityName;

    public StepGroup(int id, String name, String description, String entityName) {
        super(id, name);
        this.description = description;
        this.entityName = entityName;
    }

    public String getDescription() {
        return description;
    }

    public String getEntityName() {
        return entityName;
    }

    public String entityName() {
        return entityName;
    }

    // Record-style accessors
    public String description() { return getDescription(); }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("description", description);
        state.put("entityName", entityName);
        return state;
    }
}
