package com.minicad.step.model;

import java.util.List;

/**
 * Resolved EDGE_LOOP.
 *
 * @param id step id
 * @param name step label
 * @param edges oriented edges in loop order
 */
public record StepEdgeLoop(int id, String name, List<StepOrientedEdge> edges) implements StepEntity {

    /**
     * Creates an immutable edge-loop record.
     */
    public StepEdgeLoop {
        edges = List.copyOf(edges);
    }
}
