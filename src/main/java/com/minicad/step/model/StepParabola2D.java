package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Resolved PARABOLA 2D.
 *
 * @param id step id
 * @param name step label
 * @param position placement of the parabola
 * @param focalDist focal distance of the parabola
 */
public final class StepParabola2D extends AbstractStepEntity {
    private final StepAxis2Placement2D position;
    private final double focalDist;

    public StepParabola2D(int id, String name, StepAxis2Placement2D position, double focalDist) {
        super(id, name);
        this.position = position;
        this.focalDist = focalDist;
    }

    public StepAxis2Placement2D getPosition() {
        return position;
    }

    public double getFocalDist() {
        return focalDist;
    }

    // Record-style accessors
    public int id() { return getId(); }
    public String name() { return getName(); }
    public StepAxis2Placement2D position() { return getPosition(); }
    public double focalDist() { return getFocalDist(); }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("position", position);
        state.put("focalDist", focalDist);
        return state;
    }
}
