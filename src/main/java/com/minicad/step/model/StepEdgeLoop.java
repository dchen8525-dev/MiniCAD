package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Resolved EDGE_LOOP.
 *
 * @param id step id
 * @param name step label
 * @param edges oriented edges in loop order
 */
public final class StepEdgeLoop extends AbstractStepEntity implements StepLoop {
    private final List<StepOrientedEdge> edges;

    public StepEdgeLoop(int id, String name, List<StepOrientedEdge> edges) {
        super(id, name);
        this.edges = edges == null ? null : java.util.List.copyOf(edges);
    }

    public List<StepOrientedEdge> getEdges() {
        return edges;
    }

    // Record-style accessors
    public int id() { return getId(); }
    public String name() { return getName(); }
    public List<StepOrientedEdge> edges() { return getEdges(); }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("edges", edges);
        return state;
    }
}
