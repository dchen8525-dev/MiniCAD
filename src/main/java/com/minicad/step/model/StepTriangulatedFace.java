package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Resolved TRIANGULATED_FACE.
 * A face represented by a triangulated surface with coordinate references.
 */
public final class StepTriangulatedFace extends AbstractStepEntity {
    private final List<StepEntity> vertices;
    private final List<Integer> indices;

    public StepTriangulatedFace(int id, String name, List<StepEntity> vertices, List<Integer> indices) {
        super(id, name);
        this.vertices = vertices == null ? null : java.util.List.copyOf(vertices);
        this.indices = indices == null ? null : java.util.List.copyOf(indices);
    }

    public List<StepEntity> getVertices() {
        return vertices;
    }

    public List<Integer> getIndices() {
        return indices;
    }

    // Record-style accessors
    public int id() { return getId(); }
    public String name() { return getName(); }
    public List<StepEntity> vertices() { return getVertices(); }
    public List<Integer> indices() { return getIndices(); }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("vertices", vertices);
        state.put("indices", indices);
        return state;
    }
}
