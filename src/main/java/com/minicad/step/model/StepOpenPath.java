package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Resolved OPEN_PATH.
 *
 * @param id STEP id
 * @param name STEP label
 * @param edges oriented edges in path order
 */
public final class StepOpenPath extends AbstractStepEntity {
    private final List<StepOrientedEdge> edges;

    public StepOpenPath(int id, String name, List<StepOrientedEdge> edges) {
        super(id, name);
        this.edges = edges == null ? null : java.util.List.copyOf(edges);
    }

    public List<StepOrientedEdge> getEdges() {
        return edges;
    }

    // Record-style accessor
    public List<StepOrientedEdge> edges() {
        return edges;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("edges", edges);
        return state;
    }
}
