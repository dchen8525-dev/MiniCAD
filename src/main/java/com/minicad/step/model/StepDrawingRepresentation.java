package com.minicad.step.model;

import java.util.List;
import java.util.Map;

/**
 * Resolved DRAWING_REPRESENTATION.
 */
public final class StepDrawingRepresentation extends AbstractStepEntity {
    private final StepEntity context;
    private final List<StepEntity> items;

    public StepDrawingRepresentation(int id, String name, StepEntity context, List<StepEntity> items) {
        super(id, name);
        this.context = context;
        this.items = items == null ? null : java.util.List.copyOf(items);
    }

    public StepEntity getContext() {
        return context;
    }

    public List<StepEntity> getItems() {
        return items;
    }

    @Override
    protected Map<String, Object> components() {
        return componentsOf(
                "id", getId(), "name", getName(), "context", context, "items", items);
    }
}
