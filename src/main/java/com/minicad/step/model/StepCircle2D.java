package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Resolved CIRCLE_2D.
 * A circle in 2D parameter space.
 *
 * @param id step id
 * @param name step label
 * @param position 2D placement (center and direction)
 * @param radius circle radius
 */
public final class StepCircle2D extends AbstractStepEntity {
    private final StepAxis2Placement2D position;
    private final double radius;

    public StepCircle2D(int id, String name, StepAxis2Placement2D position, double radius) {
        super(id, name);
        this.position = position;
        this.radius = radius;
    }

    public StepAxis2Placement2D getPosition() {
        return position;
    }

    public double getRadius() {
        return radius;
    }

    // Record-style accessors
    public int id() { return getId(); }
    public String name() { return getName(); }
    public StepAxis2Placement2D position() { return getPosition(); }
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
