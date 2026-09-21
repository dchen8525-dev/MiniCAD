package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Resolved POLYLINE.
 *
 * @param id STEP instance id
 * @param name polyline name
 * @param points polyline vertices
 */
public final class StepPolyline extends AbstractStepEntity {
    private final List<StepCartesianPoint> points;

    public StepPolyline(int id, String name, List<StepCartesianPoint> points) {
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
