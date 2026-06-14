package com.minicad.step.model.product;

import com.minicad.step.model.base.StepEntity;
import java.util.List;
import java.util.Objects;

/**
 * Resolved HYBRID_SHAPE_REPRESENTATION.
 */
/**
 * Resolved HYBRID_SHAPE_REPRESENTATION.
 */
public final class StepHybridShapeRepresentation implements StepEntity {
    private final int id;
    private final String name;
    private final StepEntity context;
    private final List<StepEntity> items;

    public StepHybridShapeRepresentation(int id, String name, StepEntity context, List<StepEntity> items) {
        this.id = id;
        this.name = name;
        this.context = context;
        this.items = items == null ? null : java.util.List.copyOf(items);
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public StepEntity getContext() {
        return context;
    }

    public List<StepEntity> getItems() {
        return items;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepHybridShapeRepresentation that = (StepHybridShapeRepresentation) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(context, that.context) && Objects.equals(items, that.items);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, context, items);
    }

    @Override
    public String toString() {
        return "StepHybridShapeRepresentation{" + "id=" + id + "name=" + name + "context=" + context + "items=" + items + "}";
    }
}
