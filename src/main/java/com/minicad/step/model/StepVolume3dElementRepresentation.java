package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Resolved VOLUME_3D_ELEMENT_REPRESENTATION.
 * A representation of 3D volume finite elements.
 */
public final class StepVolume3dElementRepresentation extends AbstractStepEntity {
    private final List<StepEntity> elements;
    private final StepEntity mesh;

    public StepVolume3dElementRepresentation(int id, String name, List<StepEntity> elements, StepEntity mesh) {
        super(id, name);
        this.elements = elements == null ? null : java.util.List.copyOf(elements);
        this.mesh = mesh;
    }

    public List<StepEntity> getElements() {
        return elements;
    }

    public StepEntity getMesh() {
        return mesh;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("elements", elements);
        state.put("mesh", mesh);
        return state;
    }
}
