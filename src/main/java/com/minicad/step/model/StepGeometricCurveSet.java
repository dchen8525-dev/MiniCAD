package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Minimal geometric curve set for PMI leaders or outlines.
 *
 * @param id STEP instance id
 * @param name set name
 * @param elements supported geometric elements
 */
public final class StepGeometricCurveSet extends AbstractStepEntity {
    private final List<StepEntity> elements;

    public StepGeometricCurveSet(int id, String name, List<StepEntity> elements) {
        super(id, name);
        this.elements = elements == null ? null : java.util.List.copyOf(elements);
    }

    public List<StepEntity> getElements() {
        return elements;
    }

    // Record-style accessor
    public List<StepEntity> elements() {
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
