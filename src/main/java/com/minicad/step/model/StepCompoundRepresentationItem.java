package com.minicad.step.model;

import com.minicad.step.model.StepEntity;
import java.util.List;
import java.util.Objects;

/**
 * Resolved COMPOUND_REPRESENTATION_ITEM.
 *
 * @param id STEP instance id
 * @param name entity label
 * @param items list of representation items
 * @param entityName actual entity type name (for subtype handling)
 */
public final class StepCompoundRepresentationItem implements StepEntity {
    private final int id;
    private final String name;
    private final List<StepEntity> items;
    private final String entityName;

    public StepCompoundRepresentationItem(int id, String name, List<StepEntity> items, String entityName) {
        this.id = id;
        this.name = name;
        this.items = items == null ? null : java.util.List.copyOf(items);
        this.entityName = entityName;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<StepEntity> getItems() {
        return items;
    }

    public String getEntityName() {
        return entityName;
    }

    // Record-style accessors
    public int id() { return id; }
    public String name() { return name; }
    public List<StepEntity> items() { return items; }
    public String entityName() { return entityName; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepCompoundRepresentationItem that = (StepCompoundRepresentationItem) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(items, that.items) && Objects.equals(entityName, that.entityName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, items, entityName);
    }

    @Override
    public String toString() {
        return "StepCompoundRepresentationItem{" + "id=" + id + "name=" + name + "items=" + items + "entityName=" + entityName + "}";
    }
}
