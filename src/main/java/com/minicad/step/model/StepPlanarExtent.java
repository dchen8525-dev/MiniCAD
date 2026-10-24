package com.minicad.step.model;

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
        return componentsOf(
                "id", getId(), "name", getName(), "width", width, "height", height);
    }
}
