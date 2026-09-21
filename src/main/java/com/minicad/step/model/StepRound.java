package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Resolved ROUND.
 * Represents a round/fillet feature in manufacturing.
 *
 * @param id STEP instance id
 * @param name round name
 * @param edges edges being rounded
 * @param radius fillet radius
 */
public final class StepRound extends AbstractStepEntity {
    private final List<StepEntity> edges;
    private final Double radius;

    public StepRound(int id, String name, List<StepEntity> edges, Double radius) {
        super(id, name);
        this.edges = edges == null ? null : java.util.List.copyOf(edges);
        this.radius = radius;
    }

    public List<StepEntity> getEdges() {
        return edges;
    }

    public Double getRadius() {
        return radius;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("edges", edges);
        state.put("radius", radius);
        return state;
    }
}
