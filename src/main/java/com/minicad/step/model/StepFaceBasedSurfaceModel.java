package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Resolved FACE_BASED_SURFACE_MODEL.
 *
 * @param id STEP id
 * @param name STEP label
 * @param faceSets connected face sets
 */
public final class StepFaceBasedSurfaceModel extends AbstractStepEntity {
    private final List<StepEntity> faceSets;

    public StepFaceBasedSurfaceModel(int id, String name, List<StepEntity> faceSets) {
        super(id, name);
        this.faceSets = faceSets == null ? null : java.util.List.copyOf(faceSets);
    }

    public List<StepEntity> getFaceSets() {
        return faceSets;
    }

    // Record-style accessor
    public List<StepEntity> faceSets() {
        return faceSets;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("faceSets", faceSets);
        return state;
    }
}
