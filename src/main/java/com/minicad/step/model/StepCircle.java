package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Resolved CIRCLE.
 *
 * @param id step id
 * @param name step label
 * @param position circle placement
 * @param radius radius value
 */
public final class StepCircle extends AbstractStepEntity {
    private final StepEntity position;
    private final double radius;

    public StepCircle(int id, String name, StepEntity position, double radius) {
        super(id, name);
        this.position = position;
        this.radius = radius;
    }

    public StepEntity getPosition() {
        return position;
    }

    public double getRadius() {
        return radius;
    }

    // Record-style accessors
    public int id() { return getId(); }
    public String name() { return getName(); }
    public StepEntity position() { return getPosition(); }
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
