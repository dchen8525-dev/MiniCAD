package com.minicad.step.model;

import com.minicad.step.model.core.base.StepEntity;
import java.util.List;
import java.util.Objects;

/**
 * Resolved ORDINATE_DIMENSION_REPRESENTATION.
 * An ordinate dimension representation entity.
 *
 * @param id STEP instance id
 * * @param name representation name
 * @param items representation items
 * * @param context representation context
 * @param ordinateOrigin ordinate origin point
 * @param ordinateDirection ordinate direction
 */
/**
 * Resolved ORDINATE_DIMENSION_REPRESENTATION.
 * An ordinate dimension representation entity.
 *
 * @param id STEP instance id
 * * @param name representation name
 * @param items representation items
 * * @param context representation context
 * @param ordinateOrigin ordinate origin point
 * @param ordinateDirection ordinate direction
 */
public final class StepOrdinateDimensionRepresentation implements StepEntity {
    private final int id;
    private final String name;
    private final List<StepEntity> items;
    private final StepEntity context;
    private final StepEntity ordinateOrigin;
    private final StepEntity ordinateDirection;

    public StepOrdinateDimensionRepresentation(int id, String name, List<StepEntity> items, StepEntity context, StepEntity ordinateOrigin, StepEntity ordinateDirection) {
        this.id = id;
        this.name = name;
        this.items = items == null ? null : java.util.List.copyOf(items);
        this.context = context;
        this.ordinateOrigin = ordinateOrigin;
        this.ordinateDirection = ordinateDirection;
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

    public StepEntity getOrdinateOrigin() {
        return ordinateOrigin;
    }

    public StepEntity getOrdinateDirection() {
        return ordinateDirection;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepOrdinateDimensionRepresentation that = (StepOrdinateDimensionRepresentation) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(items, that.items) && Objects.equals(context, that.context) && Objects.equals(ordinateOrigin, that.ordinateOrigin) && Objects.equals(ordinateDirection, that.ordinateDirection);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, items, context, ordinateOrigin, ordinateDirection);
    }

    @Override
    public String toString() {
        return "StepOrdinateDimensionRepresentation{" + "id=" + id + "name=" + name + "items=" + items + "context=" + context + "ordinateOrigin=" + ordinateOrigin + "ordinateDirection=" + ordinateDirection + "}";
    }
}