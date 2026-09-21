package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Resolved SPHERE_VOLUME.
 * A CSG sphere primitive volume.
 */
public final class StepSphereVolume extends AbstractStepEntity {
    private final StepEntity center;
    private final Double radius;

    public StepSphereVolume(int id, String name, StepEntity center, Double radius) {
        super(id, name);
        this.center = center;
        this.radius = radius;
    }

    public StepEntity getCenter() {
        return center;
    }

    public Double getRadius() {
        return radius;
    }

    // Record-style accessors
    public int id() { return getId(); }
    public String name() { return getName(); }
    public StepEntity center() { return getCenter(); }
    public Double radius() { return getRadius(); }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("center", center);
        state.put("radius", radius);
        return state;
    }
}
