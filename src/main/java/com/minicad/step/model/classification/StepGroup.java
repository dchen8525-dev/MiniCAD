package com.minicad.step.model.classification;

import com.minicad.step.model.base.StepEntity;
import java.util.Objects;
/**
 * Minimal GROUP metadata.
 *
 * @param id STEP instance id
 * @param name group name
 * @param description group description
 * @param entityName concrete STEP entity name
 */
/**
 * Minimal GROUP metadata.
 *
 * @param id STEP instance id
 * @param name group name
 * @param description group description
 * @param entityName concrete STEP entity name
 */
public final class StepGroup implements StepEntity {
    private final int id;
    private final String name;
    private final String description;
    private final String entityName;

    public StepGroup(int id, String name, String description, String entityName) {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepGroup that = (StepGroup) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(description, that.description) && Objects.equals(entityName, that.entityName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, entityName);
    }

    @Override
    public String toString() {
        return "StepGroup{" + "id=" + id + "name=" + name + "description=" + description + "entityName=" + entityName + "}";
    }
}
