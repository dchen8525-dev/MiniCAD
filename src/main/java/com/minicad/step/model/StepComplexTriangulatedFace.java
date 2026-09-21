package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Resolved COMPLEX_TRIANGULATED_FACE.
 * A triangulated face with multiple outer and inner boundaries.
 */
public final class StepComplexTriangulatedFace extends AbstractStepEntity {
    private final List<StepEntity> boundaries;
    private final List<StepEntity> vertices;

    public StepComplexTriangulatedFace(int id, String name, List<StepEntity> boundaries, List<StepEntity> vertices) {
        super(id, name);
        this.boundaries = boundaries == null ? null : java.util.List.copyOf(boundaries);
        this.vertices = vertices == null ? null : java.util.List.copyOf(vertices);
    }

    public List<StepEntity> getBoundaries() {
        return boundaries;
    }

    public List<StepEntity> getVertices() {
        return vertices;
    }

    // Record-style accessors
    public int id() { return getId(); }
    public String name() { return getName(); }
    public List<StepEntity> boundaries() { return getBoundaries(); }
    public List<StepEntity> vertices() { return getVertices(); }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("boundaries", boundaries);
        state.put("vertices", vertices);
        return state;
    }
}
