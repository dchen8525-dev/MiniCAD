package com.minicad.step.model.geometry;

import com.minicad.step.model.base.StepEntity;
import java.util.List;

import com.minicad.step.model.topology.StepOrientedEdge;

/**
 * Resolved ORIENTED_PATH.
 *
 * @param id STEP id
 * @param name STEP label
 * @param pathElement referenced path-like element
 * @param orientation whether the oriented path agrees with the referenced path orientation
 * @param edges derived oriented-edge list
 */
public record StepOrientedPath(
        int id,
        String name,
        StepEntity pathElement,
        boolean orientation,
        List<StepOrientedEdge> edges
) implements StepEntity {

    public StepOrientedPath {
        edges = List.copyOf(edges);
    }
}
