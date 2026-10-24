package com.minicad.step.model;

import java.util.Map;

/**
 * Resolved VERTEX_POINT.
 *
 * @param id step id
 * @param name step label
 * @param point referenced point geometry
 */
public final class StepVertexPoint extends AbstractStepEntity {
    private final StepCartesianPoint point;

    public StepVertexPoint(int id, String name, StepCartesianPoint point) {
        super(id, name);
        this.point = point;
    }

    public StepCartesianPoint getPoint() {
        return point;
    }

    // Record-style accessors
    public int id() { return getId(); }
    public String name() { return getName(); }
    public StepCartesianPoint point() { return getPoint(); }

    @Override
    protected Map<String, Object> components() {
        return componentsOf(
                "id", getId(), "name", getName(), "point", point);
    }
}
