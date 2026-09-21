package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Resolved CUBIC_BEZIER_TRIANGULATED_FACE.
 * A triangulated face where edges are represented by cubic Bezier curves.
 */
public final class StepCubicBezierTriangulatedFace extends AbstractStepEntity {
    private final List<StepEntity> controlPoints;
    private final List<Integer> indices;

    public StepCubicBezierTriangulatedFace(int id, String name, List<StepEntity> controlPoints, List<Integer> indices) {
        super(id, name);
        this.controlPoints = controlPoints == null ? null : java.util.List.copyOf(controlPoints);
        this.indices = indices == null ? null : java.util.List.copyOf(indices);
    }

    public List<StepEntity> getControlPoints() {
        return controlPoints;
    }

    public List<Integer> getIndices() {
        return indices;
    }

    // Record-style accessors
    public int id() { return getId(); }
    public String name() { return getName(); }
    public List<StepEntity> controlPoints() { return getControlPoints(); }
    public List<Integer> indices() { return getIndices(); }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("controlPoints", controlPoints);
        state.put("indices", indices);
        return state;
    }
}
