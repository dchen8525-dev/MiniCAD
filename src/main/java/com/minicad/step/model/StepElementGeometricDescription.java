package com.minicad.step.model;

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
        return componentsOf(
                "id", getId(), "name", getName(), "description", description, "elementVolume", elementVolume);
    }
}
