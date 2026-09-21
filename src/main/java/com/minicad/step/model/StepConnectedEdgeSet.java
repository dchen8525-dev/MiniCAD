package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Resolved CONNECTED_EDGE_SET.
 *
 * @param id STEP id
 * @param name STEP label
 * @param edges member edges
 */
public final class StepConnectedEdgeSet extends AbstractStepEntity {
    private final List<StepEntity> edges;

    public StepConnectedEdgeSet(int id, String name, List<StepEntity> edges) {
        super(id, name);
        this.edges = edges == null ? null : java.util.List.copyOf(edges);
    }

    public List<StepEntity> getEdges() {
        return edges;
    }

    // Record-style accessor
    public List<StepEntity> edges() {
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
