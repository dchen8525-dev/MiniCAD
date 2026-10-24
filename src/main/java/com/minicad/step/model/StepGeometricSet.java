package com.minicad.step.model;

import java.util.List;
import java.util.Map;

/**
 * Minimal geometric set.
 *
 * @param id STEP instance id
 * @param name set name
 * @param elements supported geometric elements
 */
public final class StepGeometricSet extends AbstractStepEntity {
    private final List<StepEntity> elements;

    public StepGeometricSet(int id, String name, List<StepEntity> elements) {
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
        return componentsOf(
                "id", getId(), "name", getName(), "elements", elements);
    }
}
