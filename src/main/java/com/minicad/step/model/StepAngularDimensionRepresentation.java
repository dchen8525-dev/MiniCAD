package com.minicad.step.model;

import java.util.List;
import java.util.Map;

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
public final class StepAngularDimensionRepresentation extends AbstractStepEntity {
    private final List<StepEntity> items;
    private final StepEntity context;
    private final Double angleValue;
    private final StepEntity angleUnit;

    public StepAngularDimensionRepresentation(int id, String name, List<StepEntity> items, StepEntity context, Double angleValue, StepEntity angleUnit) {
        super(id, name);
        this.items = items == null ? null : java.util.List.copyOf(items);
        this.context = context;
        this.angleValue = angleValue;
        this.angleUnit = angleUnit;
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
    protected Map<String, Object> components() {
        return componentsOf(
                "id", getId(), "name", getName(), "items", items, "context", context,
                "angleValue", angleValue, "angleUnit", angleUnit);
    }
}
