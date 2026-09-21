package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Resolved SHAPE_DIMENSION_REPRESENTATION.
 * A representation of dimensional information for a shape.
 *
 * @param id STEP instance id
 * @param name representation name
 * @param items dimension items
 * @param context representation context
 */
public final class StepShapeDimensionRepresentation extends AbstractStepEntity {
    private final List<StepEntity> items;
    private final StepEntity context;

    public StepShapeDimensionRepresentation(int id, String name, List<StepEntity> items, StepEntity context) {
        super(id, name);
        this.items = items == null ? null : java.util.List.copyOf(items);
        this.context = context;
    }

    public List<StepEntity> getItems() {
        return items;
    }

    public StepEntity getContext() {
        return context;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("items", items);
        state.put("context", context);
        return state;
    }
}
