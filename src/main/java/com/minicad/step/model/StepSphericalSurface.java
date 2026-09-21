package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Minimal spherical surface semantic record.
 *
 * @param id STEP instance id
 * @param name STEP label
 * @param position sphere placement
 * @param radius sphere radius
 */
public final class StepSphericalSurface extends AbstractStepEntity {
    private final StepAxis2Placement3D position;
    private final double radius;

    public StepSphericalSurface(int id, String name, StepAxis2Placement3D position, double radius) {
        super(id, name);
        this.position = position;
        this.radius = radius;
    }

    public StepAxis2Placement3D getPosition() {
        return position;
    }

    public double getRadius() {
        return radius;
    }

    // Record-style accessors
    public int id() { return getId(); }
    public String name() { return getName(); }
    public StepAxis2Placement3D position() { return getPosition(); }
    public double radius() { return getRadius(); }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("position", position);
        state.put("radius", radius);
        return state;
    }
}
