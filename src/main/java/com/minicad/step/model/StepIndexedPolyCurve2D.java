package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Resolved INDEXED_POLY_CURVE_2D.
 *
 * @param id step id
 * @param name step label
 * @param points control points
 * @param indices indices into the point list defining the poly curve
 */
public final class StepIndexedPolyCurve2D extends AbstractStepEntity {
    private final List<StepCartesianPoint> points;
    private final List<Integer> indices;

    public StepIndexedPolyCurve2D(int id, String name, List<StepCartesianPoint> points, List<Integer> indices) {
        super(id, name);
        this.points = points == null ? null : java.util.List.copyOf(points);
        this.indices = indices == null ? null : java.util.List.copyOf(indices);
    }

    public List<StepCartesianPoint> getPoints() {
        return points;
    }

    public List<Integer> getIndices() {
        return indices;
    }

    // Record-style accessors
    public List<StepCartesianPoint> points() { return getPoints(); }
    public List<Integer> indices() { return getIndices(); }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("points", points);
        state.put("indices", indices);
        return state;
    }
}
