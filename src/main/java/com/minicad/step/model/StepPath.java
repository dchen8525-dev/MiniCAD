package com.minicad.step.model;

import java.util.List;

/**
 * Resolved PATH.
 *
 * @param id STEP id
 * @param name STEP label
 * @param edges oriented edges in path order
 */
public record StepPath(int id, String name, List<StepOrientedEdge> edges) implements StepEntity {

    public StepPath {
        edges = List.copyOf(edges);
    }
}
