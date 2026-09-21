package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Resolved POLYLINE_2D.
 *
 * @param id step id
 * @param name step label
 * @param points ordered list of points defining the polyline
 */
public final class StepPolyline2D extends AbstractStepEntity {
    private final List<StepCartesianPoint> points;

    public StepPolyline2D(int id, String name, List<StepCartesianPoint> points) {
        super(id, name);
        this.points = points == null ? null : java.util.List.copyOf(points);
    }

    public List<StepCartesianPoint> getPoints() {
        return points;
    }

    // Record-style accessors
    public int id() { return getId(); }
    public String name() { return getName(); }
    public List<StepCartesianPoint> points() { return getPoints(); }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("points", points);
        return state;
    }
}
