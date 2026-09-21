package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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
public final class StepLinearDimensionRepresentation extends AbstractStepEntity {
    private final List<StepEntity> items;
    private final StepEntity context;
    private final Double lengthValue;
    private final StepEntity lengthUnit;

    public StepLinearDimensionRepresentation(int id, String name, List<StepEntity> items, StepEntity context, Double lengthValue, StepEntity lengthUnit) {
        super(id, name);
        this.items = items == null ? null : java.util.List.copyOf(items);
        this.context = context;
        this.lengthValue = lengthValue;
        this.lengthUnit = lengthUnit;
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
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("items", items);
        state.put("context", context);
        state.put("lengthValue", lengthValue);
        state.put("lengthUnit", lengthUnit);
        return state;
    }
}
