package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.Map;

public final class StepPlanarExtent extends AbstractStepEntity {
    private final double width;
    private final double height;

    public StepPlanarExtent(int id, String name, double width, double height) {
        super(id, name);
        this.width = width;
        this.height = height;
    }

    public double getWidth() {
        return width;
    }

    public double getHeight() {
        return height;
    }

    // Record-style accessors
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
        state.put("width", width);
        state.put("height", height);
        return state;
    }
}
