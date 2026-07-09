package com.minicad.step.model;

import com.minicad.step.model.core.base.StepEntity;
import java.util.List;
import java.util.Objects;

/**
 * Resolved SHAPE_DIMENSION_REPRESENTATION.
 * A representation of dimensional information for a shape.
 *
 * @param id STEP instance id
 * @param name representation name
 * @param items dimension items
 * @param context representation context
 */
/**
 * Resolved SHAPE_DIMENSION_REPRESENTATION.
 * A representation of dimensional information for a shape.
 *
 * @param id STEP instance id
 * @param name representation name
 * @param items dimension items
 * @param context representation context
 */
public final class StepShapeDimensionRepresentation implements StepEntity {
    private final int id;
    private final String name;
    private final List<StepEntity> items;
    private final StepEntity context;

    public StepShapeDimensionRepresentation(int id, String name, List<StepEntity> items, StepEntity context) {
        this.id = id;
        this.name = name;
        this.items = items == null ? null : java.util.List.copyOf(items);
        this.context = context;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepShapeDimensionRepresentation that = (StepShapeDimensionRepresentation) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(items, that.items) && Objects.equals(context, that.context);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, items, context);
    }

    @Override
    public String toString() {
        return "StepShapeDimensionRepresentation{" + "id=" + id + "name=" + name + "items=" + items + "context=" + context + "}";
    }
}
