package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Resolved ELEMENT_SET.
 * A named set of finite elements.
 */
public final class StepElementSet extends AbstractStepEntity {
    private final List<StepEntity> elements;

    public StepElementSet(int id, String name, List<StepEntity> elements) {
        super(id, name);
        this.elements = elements == null ? null : java.util.List.copyOf(elements);
    }

    public List<StepEntity> getElements() {
        return elements;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("elements", elements);
        return state;
    }
}
