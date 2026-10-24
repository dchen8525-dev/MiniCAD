package com.minicad.step.model;

import java.util.Map;

public final class StepPresentationLayerUsage extends AbstractStepEntity {
    private final String description;
    private final StepEntity layer;

    public StepPresentationLayerUsage(int id, String name, String description, StepEntity layer) {
        super(id, name);
        this.description = description;
        this.layer = layer;
    }

    public String getDescription() {
        return description;
    }

    public StepEntity getLayer() {
        return layer;
    }

    @Override
    protected Map<String, Object> components() {
        return componentsOf(
                "id", getId(), "name", getName(), "description", description, "layer", layer);
    }
}
