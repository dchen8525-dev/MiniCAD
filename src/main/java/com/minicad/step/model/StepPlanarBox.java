package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.Map;

public final class StepPlanarBox extends AbstractStepEntity {
    private final StepEntity placement;
    private final double width;
    private final double height;

    public StepPlanarBox(int id, String name, StepEntity placement, double width, double height) {
        super(id, name);
        this.placement = placement;
        this.width = width;
        this.height = height;
    }

    public StepEntity getPlacement() {
        return placement;
    }

    public double getWidth() {
        return width;
    }

    public double getHeight() {
        return height;
    }

    // Record-style accessors
    public StepEntity placement() {
        return placement;
    }

    public double width() {
        return width;
    }

    public double height() {
        return height;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("placement", placement);
        state.put("width", width);
        state.put("height", height);
        return state;
    }
}
