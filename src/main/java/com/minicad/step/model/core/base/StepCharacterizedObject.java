package com.minicad.step.model.core.base;

import java.util.Objects;

/**
 * Minimal CHARACTERIZED_OBJECT/FEATURE_DEFINITION metadata.
 *
 * @param id STEP instance id
 * @param name object name
 * @param description object description
 * @param entityName concrete STEP entity name
 */
/**
 * Minimal CHARACTERIZED_OBJECT/FEATURE_DEFINITION metadata.
 *
 * @param id STEP instance id
 * @param name object name
 * @param description object description
 * @param entityName concrete STEP entity name
 */
public final class StepCharacterizedObject implements StepEntity {
    private final int id;
    private final String name;
    private final String description;
    private final String entityName;

    public StepCharacterizedObject(int id, String name, String description, String entityName) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.entityName = entityName;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepCharacterizedObject that = (StepCharacterizedObject) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(description, that.description) && Objects.equals(entityName, that.entityName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, entityName);
    }

    @Override
    public String toString() {
        return "StepCharacterizedObject{" + "id=" + id + "name=" + name + "description=" + description + "entityName=" + entityName + "}";
    }
}
