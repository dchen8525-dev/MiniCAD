package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Resolved SECTION_REPRESENTATION.
 */
public final class StepSectionRepresentation extends AbstractStepEntity {
    private final StepEntity context;
    private final List<StepEntity> items;

    public StepSectionRepresentation(int id, String name, StepEntity context, List<StepEntity> items) {
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
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("context", context);
        state.put("items", items);
        return state;
    }
}
