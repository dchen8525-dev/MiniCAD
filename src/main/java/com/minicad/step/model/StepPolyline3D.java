package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Resolved POLYLINE_3D.
 */
public final class StepPolyline3D extends AbstractStepEntity {
    private final List<StepEntity> points;

    public StepPolyline3D(int id, String name, List<StepEntity> points) {
        super(id, name);
        this.points = points == null ? null : java.util.List.copyOf(points);
    }

    public List<StepEntity> getPoints() {
        return points;
    }

    // Record-style accessor
    public List<StepEntity> points() { return getPoints(); }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("points", points);
        return state;
    }
}
