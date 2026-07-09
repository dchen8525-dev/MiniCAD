package com.minicad.step.model;

import com.minicad.step.model.core.base.StepEntity;
import java.util.Objects;

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
public final class StepGenericEntity implements StepEntity {
    private final int id;
    private final String name;
    private final String entityName;
    private final Object[] attributes; // Flexible attributes array

    // Constructor for simple entities (1 attribute)
    public StepGenericEntity(int id, String name, String entityName) {
        this.id = id;
        this.name = name;
        this.entityName = entityName;
        this.attributes = new Object[0];
    }

    // Constructor for single-attribute entities
    public StepGenericEntity(int id, String name, Object attribute1, String entityName) {
        this.id = id;
        this.name = name;
        this.entityName = entityName;
        this.attributes = new Object[]{attribute1};
    }

    // Constructor for two-attribute entities
    public StepGenericEntity(int id, String name, Object attribute1, Object attribute2, String entityName) {
        this.id = id;
        this.name = name;
        this.entityName = entityName;
        this.attributes = new Object[]{attribute1, attribute2};
    }

    // Constructor for three-attribute entities
    public StepGenericEntity(int id, String name, Object attribute1, Object attribute2, Object attribute3, String entityName) {
        this.id = id;
        this.name = name;
        this.entityName = entityName;
        this.attributes = new Object[]{attribute1, attribute2, attribute3};
    }

    // Constructor for four-attribute entities (relationship pattern)
    public StepGenericEntity(int id, String name, Object attribute1, Object attribute2, Object attribute3, Object attribute4, String entityName) {
        this.id = id;
        this.name = name;
        this.entityName = entityName;
        this.attributes = new Object[]{attribute1, attribute2, attribute3, attribute4};
    }

    // Constructor for flexible number of attributes
    public StepGenericEntity(int id, String name, Object[] attributes, String entityName) {
        this.id = id;
        this.name = name;
        this.entityName = entityName;
        this.attributes = attributes != null ? attributes : new Object[0];
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepGenericEntity that = (StepGenericEntity) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(id);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("StepGenericEntity{");
        sb.append("id=").append(id);
        sb.append(", name='").append(name).append('\'');
        sb.append(", entityName='").append(entityName).append('\'');
        sb.append(", attributes=[");
        for (int i = 0; i < attributes.length; i++) {
            if (i > 0) sb.append(", ");
            sb.append(attributes[i]);
        }
        sb.append("]}");
        return sb.toString();
    }
}