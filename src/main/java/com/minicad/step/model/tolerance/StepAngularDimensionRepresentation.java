package com.minicad.step.model.tolerance;

import com.minicad.step.model.base.StepEntity;
import java.util.List;
import java.util.Objects;

/**
 * Resolved ANGULAR_DIMENSION_REPRESENTATION.
 * An angular dimension representation entity.
 *
 * @param id STEP instance id
 * @param name representation name
 * @param items representation items
 * * @param context representation context
 * @param angleValue angle value
 * @param angleUnit angle unit
 */
/**
 * Resolved ANGULAR_DIMENSION_REPRESENTATION.
 * An angular dimension representation entity.
 *
 * @param id STEP instance id
 * @param name representation name
 * @param items representation items
 * * @param context representation context
 * @param angleValue angle value
 * @param angleUnit angle unit
 */
public final class StepAngularDimensionRepresentation implements StepEntity {
    private final int id;
    private final String name;
    private final List<StepEntity> items;
    private final StepEntity context;
    private final Double angleValue;
    private final StepEntity angleUnit;

    public StepAngularDimensionRepresentation(int id, String name, List<StepEntity> items, StepEntity context, Double angleValue, StepEntity angleUnit) {
        this.id = id;
        this.name = name;
        this.items = items == null ? null : java.util.List.copyOf(items);
        this.context = context;
        this.angleValue = angleValue;
        this.angleUnit = angleUnit;
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

    public Double getAngleValue() {
        return angleValue;
    }

    public StepEntity getAngleUnit() {
        return angleUnit;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepAngularDimensionRepresentation that = (StepAngularDimensionRepresentation) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(items, that.items) && Objects.equals(context, that.context) && Objects.equals(angleValue, that.angleValue) && Objects.equals(angleUnit, that.angleUnit);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, items, context, angleValue, angleUnit);
    }

    @Override
    public String toString() {
        return "StepAngularDimensionRepresentation{" + "id=" + id + "name=" + name + "items=" + items + "context=" + context + "angleValue=" + angleValue + "angleUnit=" + angleUnit + "}";
    }
}