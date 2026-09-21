package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Minimal TESSELLATED_FACE.
 * A face defined by a tessellated (triangulated) surface.
 *
 * @param id STEP id
 * @param name STEP label
 * @param triangles list of triangle entities or vertex references
 */
public final class StepTessellatedFace extends AbstractStepEntity {
    private final List<StepEntity> triangles;

    public StepTessellatedFace(int id, String name, List<StepEntity> triangles) {
        super(id, name);
        this.triangles = triangles == null ? null : java.util.List.copyOf(triangles);
    }

    public List<StepEntity> getTriangles() {
        return triangles;
    }

    // Record-style accessors
    public int id() { return getId(); }
    public String name() { return getName(); }
    public List<StepEntity> triangles() { return getTriangles(); }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("triangles", triangles);
        return state;
    }
}
