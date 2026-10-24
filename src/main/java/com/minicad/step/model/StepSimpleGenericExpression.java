package com.minicad.step.model;

import java.util.Map;

/**
 * SIMPLE_GENERIC_EXPRESSION entity model.
 * Simple generic expression (literal or simple value).
 *
 * @param id STEP instance id
 * @param name entity label
 * @param entityName actual entity type name (for subtype handling)
 */
public final class StepSimpleGenericExpression extends AbstractStepEntity {
    private final String entityName;

    public StepSimpleGenericExpression(
        int id,
        String name,
        String entityName) {
        super(id, name);
        this.entityName = entityName;
    }

    public String getEntityName() {
        return entityName;
    }

    public String entityName() {
        return entityName;
    }

    @Override
    protected Map<String, Object> components() {
        return componentsOf(
                "id", getId(), "name", getName(), "entityName", entityName);
    }
}
