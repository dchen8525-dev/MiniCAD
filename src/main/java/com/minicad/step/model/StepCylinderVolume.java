package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Resolved CYLINDER_VOLUME.
 * A CSG cylinder primitive volume.
 */
public final class StepCylinderVolume extends AbstractStepEntity {
    private final StepEntity position;
    private final Double radius;
    private final Double height;

    public StepCylinderVolume(int id, String name, StepEntity position, Double radius, Double height) {
        super(id, name);
        this.position = position;
        this.radius = radius;
        this.height = height;
    }

    public StepEntity getPosition() {
        return position;
    }

    public Double getRadius() {
        return radius;
    }

    public Double getHeight() {
        return height;
    }

    // Record-style accessors
    public int id() { return getId(); }
    public String name() { return getName(); }
    public StepEntity position() { return getPosition(); }
    public Double radius() { return getRadius(); }
    public Double height() { return getHeight(); }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("position", position);
        state.put("radius", radius);
        state.put("height", height);
        return state;
    }
}
