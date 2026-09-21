package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Resolved HYPERBOLOID_SURFACE.
 * A quadric surface defined by a hyperboloid shape (one-sheet or two-sheet).
 */
public final class StepHyperboloidSurface extends AbstractStepEntity {
    private final StepEntity position;
    private final Double radius;
    private final Double semiAxis;

    public StepHyperboloidSurface(int id, String name, StepEntity position, Double radius, Double semiAxis) {
        super(id, name);
        this.position = position;
        this.radius = radius;
        this.semiAxis = semiAxis;
    }

    public StepEntity getPosition() {
        return position;
    }

    public Double getRadius() {
        return radius;
    }

    public Double getSemiAxis() {
        return semiAxis;
    }

    // Record-style accessors
    public StepEntity position() { return getPosition(); }
    public Double radius() { return getRadius(); }
    public Double semiAxis() { return getSemiAxis(); }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("position", position);
        state.put("radius", radius);
        state.put("semiAxis", semiAxis);
        return state;
    }
}
