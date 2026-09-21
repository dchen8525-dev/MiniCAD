package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Resolved LINE.
 *
 * @param id step id
 * @param name step label
 * @param point line origin
 * @param vector line direction vector
 */
public final class StepLine extends AbstractStepEntity {
    private final StepCartesianPoint point;
    private final StepVector vector;

    public StepLine(int id, String name, StepCartesianPoint point, StepVector vector) {
        super(id, name);
        this.point = point;
        this.vector = vector;
    }

    public StepCartesianPoint getPoint() {
        return point;
    }

    public StepVector getVector() {
        return vector;
    }

    // Record-style accessors
    public int id() { return getId(); }
    public String name() { return getName(); }
    public StepCartesianPoint point() { return getPoint(); }
    public StepVector vector() { return getVector(); }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("point", point);
        state.put("vector", vector);
        return state;
    }
}
