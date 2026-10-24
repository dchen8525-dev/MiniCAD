package com.minicad.step.model;

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
        return componentsOf(
                "id", getId(), "name", getName(), "description", description, "camera", camera);
    }
}
