package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Resolved CONNECTED_FACE_SUB_SET.
 *
 * @param id STEP id
 * @param name STEP label
 * @param faces subset faces
 * @param parentFaceSet parent connected face set
 */
public final class StepConnectedFaceSubSet extends AbstractStepEntity {
    private final List<StepFaceEntity> faces;
    private final StepEntity parentFaceSet;

    public StepConnectedFaceSubSet(int id, String name, List<StepFaceEntity> faces, StepEntity parentFaceSet) {
        super(id, name);
        this.faces = faces == null ? null : java.util.List.copyOf(faces);
        this.parentFaceSet = parentFaceSet;
    }

    public List<StepFaceEntity> getFaces() {
        return faces;
    }

    // Record-style accessor
    public List<StepFaceEntity> faces() {
        return faces;
    }

    public StepEntity getParentFaceSet() {
        return parentFaceSet;
    }

    // Record-style accessor
    public StepEntity parentFaceSet() {
        return parentFaceSet;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("faces", faces);
        state.put("parentFaceSet", parentFaceSet);
        return state;
    }
}
