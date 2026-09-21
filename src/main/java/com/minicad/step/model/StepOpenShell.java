package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Resolved OPEN_SHELL.
 *
 * @param id step id
 * @param name step label
 * @param faces shell faces
 */
public final class StepOpenShell extends AbstractStepEntity {
    private final List<StepFaceEntity> faces;

    public StepOpenShell(int id, String name, List<StepFaceEntity> faces) {
        super(id, name);
        this.faces = faces == null ? null : java.util.List.copyOf(faces);
    }

    public List<StepFaceEntity> getFaces() {
        return faces;
    }

    // Record-style accessor
    public List<StepFaceEntity> faces() {
        return faces;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("faces", faces);
        return state;
    }
}
