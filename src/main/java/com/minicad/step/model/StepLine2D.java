package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Resolved LINE_2D.
 *
 * @param id step id
 * @param name step label
 * @param point_2d point on the line
 * @param direction_2d direction of the line
 */
public final class StepLine2D extends AbstractStepEntity {
    private final StepCartesianPoint point2d;
    private final StepDirection direction2d;

    public StepLine2D(int id, String name, StepCartesianPoint point2d, StepDirection direction2d) {
        super(id, name);
        this.point2d = point2d;
        this.direction2d = direction2d;
    }

    public StepCartesianPoint getPoint2d() {
        return point2d;
    }

    public StepDirection getDirection2d() {
        return direction2d;
    }

    // Record-style accessors
    public int id() { return getId(); }
    public String name() { return getName(); }
    public StepCartesianPoint point2d() { return getPoint2d(); }
    public StepDirection direction2d() { return getDirection2d(); }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("point2d", point2d);
        state.put("direction2d", direction2d);
        return state;
    }
}
