package com.minicad.step.model;

import java.util.LinkedHashMap;
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
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("description", description);
        state.put("layer", layer);
        return state;
    }
}
