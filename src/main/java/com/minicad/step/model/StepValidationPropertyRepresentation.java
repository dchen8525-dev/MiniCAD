package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Resolved VALIDATION_PROPERTY_REPRESENTATION.
 * A representation used to validate geometric properties against a reference.
 */
public final class StepValidationPropertyRepresentation extends AbstractStepEntity {
    private final String representationType;
    private final List<StepEntity> items;
    private final StepEntity context;

    public StepValidationPropertyRepresentation(int id, String name, String representationType, List<StepEntity> items, StepEntity context) {
        super(id, name);
        this.representationType = representationType;
        this.items = items == null ? null : java.util.List.copyOf(items);
        this.context = context;
    }

    public String getRepresentationType() {
        return representationType;
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
        state.put("representationType", representationType);
        state.put("items", items);
        state.put("context", context);
        return state;
    }
}
