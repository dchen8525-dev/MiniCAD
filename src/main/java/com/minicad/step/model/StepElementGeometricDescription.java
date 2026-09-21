package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Resolved ELEMENT_GEOMETRIC_DESCRIPTION.
 * Geometric description of a finite element.
 */
public final class StepElementGeometricDescription extends AbstractStepEntity {
    private final String description;
    private final StepEntity elementVolume;

    public StepElementGeometricDescription(int id, String name, String description, StepEntity elementVolume) {
        super(id, name);
        this.description = description;
        this.elementVolume = elementVolume;
    }

    public String getDescription() {
        return description;
    }

    public StepEntity getElementVolume() {
        return elementVolume;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("description", description);
        state.put("elementVolume", elementVolume);
        return state;
    }
}
