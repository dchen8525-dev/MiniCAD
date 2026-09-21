package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Resolved MECHANISM_STATE_REPRESENTATION.
 * A representation of a mechanism in a particular configuration state.
 * Subtype of REPRESENTATION.
 */
public final class StepMechanismStateRepresentation extends AbstractStepEntity {
    private final List<StepEntity> items;
    private final StepEntity context;

    public StepMechanismStateRepresentation(int id, String name, List<StepEntity> items, StepEntity context) {
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
