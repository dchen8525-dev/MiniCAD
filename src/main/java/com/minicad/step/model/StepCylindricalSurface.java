package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Resolved CYLINDRICAL_SURFACE.
 *
 * @param id step id
 * @param name step label
 * @param position axis placement
 * @param radius radius
 */
public final class StepCylindricalSurface extends AbstractStepEntity {
    private final StepAxis2Placement3D position;
    private final double radius;

    public StepCylindricalSurface(int id, String name, StepAxis2Placement3D position, double radius) {
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
