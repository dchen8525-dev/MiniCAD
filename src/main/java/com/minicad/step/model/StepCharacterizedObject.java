package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Minimal CHARACTERIZED_OBJECT/FEATURE_DEFINITION metadata.
 *
 * @param id STEP instance id
 * @param name object name
 * @param description object description
 * @param entityName concrete STEP entity name
 */
public final class StepCharacterizedObject extends AbstractStepEntity {
    private final String description;
    private final String entityName;

    public StepCharacterizedObject(int id, String name, String description, String entityName) {
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

    // Alias for getEntityName for reflection-based entity name extraction
    public String entityName() {
        return entityName;
    }

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
