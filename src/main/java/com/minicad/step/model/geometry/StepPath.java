package com.minicad.step.model.geometry;

import com.minicad.step.model.base.StepEntity;
import java.util.List;

import com.minicad.step.model.topology.StepOrientedEdge;

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
