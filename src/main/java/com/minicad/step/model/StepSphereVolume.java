package com.minicad.step.model;

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
        return componentsOf(
                "id", getId(), "name", getName(), "center", center, "radius", radius);
    }
}
