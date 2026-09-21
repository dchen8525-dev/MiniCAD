package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Resolved INDEXED_POLY_CURVE / INDEXED_POLYCURVE (MiniCAD alias).
 */
public final class StepIndexedPolyCurve extends AbstractStepEntity {
    private final List<StepCartesianPoint> points;
    private final List<Integer> indices;
    private final boolean closed;

    public StepIndexedPolyCurve(int id, String name, List<StepCartesianPoint> points, List<Integer> indices, boolean closed) {
        super(id, name);
        this.points = points == null ? null : java.util.List.copyOf(points);
        this.indices = indices == null ? null : java.util.List.copyOf(indices);
        this.closed = closed;
    }

    public List<StepCartesianPoint> getPoints() {
        return points;
    }

    public List<Integer> getIndices() {
        return indices;
    }

    public boolean isClosed() {
        return closed;
    }

    // Record-style accessors
    public int id() { return getId(); }
    public String name() { return getName(); }
    public List<StepCartesianPoint> points() { return getPoints(); }
    public List<Integer> indices() { return getIndices(); }
    public boolean closed() { return isClosed(); }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("points", points);
        state.put("indices", indices);
        state.put("closed", closed);
        return state;
    }
}
