package com.minicad.step.model.tolerance;

import com.minicad.step.model.base.StepEntity;
import java.util.List;
import java.util.Objects;

/**
 * Resolved LINEAR_DIMENSION_REPRESENTATION.
 * A linear dimension representation entity.
 *
 * @param id STEP instance id
 * @param name representation name
 * @param items representation items
 * * @param context representation context
 * @param lengthValue length value
 * @param lengthUnit length unit
 */
/**
 * Resolved LINEAR_DIMENSION_REPRESENTATION.
 * A linear dimension representation entity.
 *
 * @param id STEP instance id
 * @param name representation name
 * @param items representation items
 * * @param context representation context
 * @param lengthValue length value
 * @param lengthUnit length unit
 */
public final class StepLinearDimensionRepresentation implements StepEntity {
    private final int id;
    private final String name;
    private final List<StepEntity> items;
    private final StepEntity context;
    private final Double lengthValue;
    private final StepEntity lengthUnit;

    public StepLinearDimensionRepresentation(int id, String name, List<StepEntity> items, StepEntity context, Double lengthValue, StepEntity lengthUnit) {
        this.id = id;
        this.name = name;
        this.items = items == null ? null : java.util.List.copyOf(items);
        this.context = context;
        this.lengthValue = lengthValue;
        this.lengthUnit = lengthUnit;
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

    public Double getLengthValue() {
        return lengthValue;
    }

    public StepEntity getLengthUnit() {
        return lengthUnit;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepLinearDimensionRepresentation that = (StepLinearDimensionRepresentation) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(items, that.items) && Objects.equals(context, that.context) && Objects.equals(lengthValue, that.lengthValue) && Objects.equals(lengthUnit, that.lengthUnit);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, items, context, lengthValue, lengthUnit);
    }

    @Override
    public String toString() {
        return "StepLinearDimensionRepresentation{" + "id=" + id + "name=" + name + "items=" + items + "context=" + context + "lengthValue=" + lengthValue + "lengthUnit=" + lengthUnit + "}";
    }
}