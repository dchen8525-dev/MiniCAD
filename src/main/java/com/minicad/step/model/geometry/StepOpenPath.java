package com.minicad.step.model.geometry;

import com.minicad.step.model.base.StepEntity;
import java.util.List;

import com.minicad.step.model.topology.StepOrientedEdge;

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
