package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Resolved PARABOLOID_SURFACE.
 * A quadric surface defined by a paraboloid shape.
 */
public final class StepParaboloidSurface extends AbstractStepEntity {
    private final StepEntity position;
    private final Double focalLength;

    public StepParaboloidSurface(int id, String name, StepEntity position, Double focalLength) {
        super(id, name);
        this.position = position;
        this.focalLength = focalLength;
    }

    public StepEntity getPosition() {
        return position;
    }

    public Double getFocalLength() {
        return focalLength;
    }

    // Record-style accessors
    public StepEntity position() { return getPosition(); }
    public Double focalLength() { return getFocalLength(); }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("position", position);
        state.put("focalLength", focalLength);
        return state;
    }
}
