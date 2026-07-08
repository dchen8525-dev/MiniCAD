package com.minicad.step.model.technical.tolerance;

import com.minicad.step.model.core.base.StepEntity;
import java.util.List;
import java.util.Objects;

/**
 * Resolved SHAPE_DIMENSION_REPRESENTATION_WITH_TOLERANCE.
 * A shape dimension representation with tolerance entity.
 *
 * @param id STEP instance id
 * @param name representation name
 * @param items representation items
 * * @param context representation context
 * @param tolerance tolerance associated with the dimension
 */
/**
 * Resolved SHAPE_DIMENSION_REPRESENTATION_WITH_TOLERANCE.
 * A shape dimension representation with tolerance entity.
 *
 * @param id STEP instance id
 * @param name representation name
 * @param items representation items
 * * @param context representation context
 * @param tolerance tolerance associated with the dimension
 */
public final class StepShapeDimensionRepresentationWithTolerance implements StepEntity {
    private final int id;
    private final String name;
    private final List<StepEntity> items;
    private final StepEntity context;
    private final StepEntity tolerance;

    public StepShapeDimensionRepresentationWithTolerance(int id, String name, List<StepEntity> items, StepEntity context, StepEntity tolerance) {
        this.id = id;
        this.name = name;
        this.items = items == null ? null : java.util.List.copyOf(items);
        this.context = context;
        this.tolerance = tolerance;
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

    public StepEntity getTolerance() {
        return tolerance;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepShapeDimensionRepresentationWithTolerance that = (StepShapeDimensionRepresentationWithTolerance) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(items, that.items) && Objects.equals(context, that.context) && Objects.equals(tolerance, that.tolerance);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, items, context, tolerance);
    }

    @Override
    public String toString() {
        return "StepShapeDimensionRepresentationWithTolerance{" + "id=" + id + "name=" + name + "items=" + items + "context=" + context + "tolerance=" + tolerance + "}";
    }
}