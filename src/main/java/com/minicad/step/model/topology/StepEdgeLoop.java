package com.minicad.step.model.topology;

import java.util.List;

/**
 * Resolved EDGE_LOOP.
 *
 * @param id step id
 * @param name step label
 * @param edges oriented edges in loop order
 */
public record StepEdgeLoop(int id, String name, List<StepOrientedEdge> edges) implements StepLoop {

    /**
     * Creates an immutable edge-loop record.
     */
    public StepEdgeLoop {
        edges = List.copyOf(edges);
    }
}
