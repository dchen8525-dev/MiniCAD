package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Minimal SEAM_EDGE.
 * A seam edge where the start and end vertices are the same (closed edge on a surface seam).
 *
 * @param id STEP id
 * @param name STEP label
 * @param edgeStart start vertex
 * @param edgeEnd end vertex (same as start for seam edges)
 */
public final class StepSeamEdge extends AbstractStepEntity {
    private final StepEntity edgeStart;
    private final StepEntity edgeEnd;

    public StepSeamEdge(int id, String name, StepEntity edgeStart, StepEntity edgeEnd) {
        super(id, name);
        this.edgeStart = edgeStart;
        this.edgeEnd = edgeEnd;
    }

    public StepEntity getEdgeStart() {
        return edgeStart;
    }

    public StepEntity getEdgeEnd() {
        return edgeEnd;
    }

    // Record-style accessors
    public int id() { return getId(); }
    public String name() { return getName(); }
    public StepEntity edgeStart() { return getEdgeStart(); }
    public StepEntity edgeEnd() { return getEdgeEnd(); }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("edgeStart", edgeStart);
        state.put("edgeEnd", edgeEnd);
        return state;
    }
}
