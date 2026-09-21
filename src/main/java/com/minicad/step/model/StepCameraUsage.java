package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.Map;

public final class StepCameraUsage extends AbstractStepEntity {
    private final String description;
    private final StepEntity camera;

    public StepCameraUsage(int id, String name, String description, StepEntity camera) {
        super(id, name);
        this.description = description;
        this.camera = camera;
    }

    public String getDescription() {
        return description;
    }

    public StepEntity getCamera() {
        return camera;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("description", description);
        state.put("camera", camera);
        return state;
    }
}
