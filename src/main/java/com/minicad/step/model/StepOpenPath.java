package com.minicad.step.model;

import java.util.List;

/**
 * Resolved OPEN_PATH.
 *
 * @param id STEP id
 * @param name STEP label
 * @param edges oriented edges in path order
 */
public record StepOpenPath(int id, String name, List<StepOrientedEdge> edges) implements StepEntity {

    public StepOpenPath {
        edges = List.copyOf(edges);
    }
}
