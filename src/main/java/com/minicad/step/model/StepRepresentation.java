package com.minicad.step.model.workflow;

import com.minicad.step.model.core.base.StepEntity;
import java.util.List;
import java.util.Objects;

/**
 * Minimal representation or shape representation.
 *
 * @param id STEP instance id
 * @param name representation name
 * @param items referenced items
 * @param context representation context
 * @param shapeRepresentation whether this entity originated from SHAPE_REPRESENTATION
 * @param entityName concrete STEP entity name
 */
/**
 * Minimal representation or shape representation.
 *
 * @param id STEP instance id
 * @param name representation name
 * @param items referenced items
 * @param context representation context
 * @param shapeRepresentation whether this entity originated from SHAPE_REPRESENTATION
 * @param entityName concrete STEP entity name
 */
public final class StepRepresentation implements StepEntity {
    private final int id;
    private final String name;
    private final List<StepEntity> items;
    private final StepEntity context;
    private final boolean shapeRepresentation;
    private final String entityName;

    public StepRepresentation(int id, String name, List<StepEntity> items, StepEntity context, boolean shapeRepresentation, String entityName) {
        this.id = id;
        this.name = name;
        this.items = items == null ? null : java.util.List.copyOf(items);
        this.context = context;
        this.shapeRepresentation = shapeRepresentation;
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

    public StepEntity getContext() {
        return context;
    }

    public boolean isShapeRepresentation() {
        return shapeRepresentation;
    }

    public String getEntityName() {
        return entityName;
    }

    // Record-style accessors
    public int id() { return getId(); }
    public String name() { return getName(); }
    public List<StepEntity> items() { return getItems(); }
    public StepEntity context() { return getContext(); }
    public boolean shapeRepresentation() { return isShapeRepresentation(); }
    public String entityName() { return getEntityName(); }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepRepresentation that = (StepRepresentation) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(items, that.items) && Objects.equals(context, that.context) && shapeRepresentation == that.shapeRepresentation && Objects.equals(entityName, that.entityName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, items, context, shapeRepresentation, entityName);
    }

    @Override
    public String toString() {
        return "StepRepresentation{" + "id=" + id + "name=" + name + "items=" + items + "context=" + context + "shapeRepresentation=" + shapeRepresentation + "entityName=" + entityName + "}";
    }
}
