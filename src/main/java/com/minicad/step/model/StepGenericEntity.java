package com.minicad.step.model;

import java.util.Arrays;
import java.util.Map;

/**
 * Generic entity model for alias family entities.
 * This class provides a flexible structure that can represent various entity types
 * with common patterns (assignment, relationship, requirement, status, property, etc.)
 *
 * @param id STEP instance id
 * @param name entity label
 * @param entityName actual entity type name (for subtype handling)
 * @param attributes flexible attributes (optional, can be null, String, or entity references)
 */
public final class StepGenericEntity extends AbstractStepEntity {
    private final String entityName;
    private final Object[] attributes; // Flexible attributes array

    // Constructor for simple entities (1 attribute)
    public StepGenericEntity(int id, String name, String entityName) {
        super(id, name);
        this.entityName = entityName;
        this.attributes = new Object[0];
    }

    // Constructor for single-attribute entities
    public StepGenericEntity(int id, String name, Object attribute1, String entityName) {
        super(id, name);
        this.entityName = entityName;
        this.attributes = new Object[]{attribute1};
    }

    // Constructor for two-attribute entities
    public StepGenericEntity(int id, String name, Object attribute1, Object attribute2, String entityName) {
        super(id, name);
        this.entityName = entityName;
        this.attributes = new Object[]{attribute1, attribute2};
    }

    // Constructor for three-attribute entities
    public StepGenericEntity(int id, String name, Object attribute1, Object attribute2, Object attribute3, String entityName) {
        super(id, name);
        this.entityName = entityName;
        this.attributes = new Object[]{attribute1, attribute2, attribute3};
    }

    // Constructor for four-attribute entities (relationship pattern)
    public StepGenericEntity(int id, String name, Object attribute1, Object attribute2, Object attribute3, Object attribute4, String entityName) {
        super(id, name);
        this.entityName = entityName;
        this.attributes = new Object[]{attribute1, attribute2, attribute3, attribute4};
    }

    // Constructor for flexible number of attributes
    public StepGenericEntity(int id, String name, Object[] attributes, String entityName) {
        super(id, name);
        this.entityName = entityName;
        this.attributes = attributes != null ? attributes : new Object[0];
    }

    public String getEntityName() {
        return entityName;
    }

    // Alias for getEntityName for reflection-based entity name extraction
    public String entityName() {
        return entityName;
    }

    public Object[] getAttributes() {
        return attributes;
    }

    public Object getAttribute(int index) {
        if (index >= 0 && index < attributes.length) {
            return attributes[index];
        }
        return null;
    }

    public int getAttributeCount() {
        return attributes.length;
    }

    @Override
    protected Map<String, Object> components() {
        return componentsOf(
                "id", getId(), "name", getName(), "entityName", entityName,
                "attributes", Arrays.asList(attributes));
    }
}
