package com.minicad.step.model;

import java.util.List;

/**
 * Resolved EDGE_BASED_WIREFRAME_MODEL.
 *
 * @param id STEP id
 * @param name STEP label
 * @param boundaries connected edge sets
 */
public record StepEdgeBasedWireframeModel(
        int id,
        String name,
        List<StepConnectedEdgeSet> boundaries
) implements StepEntity {

    public StepEdgeBasedWireframeModel {
        boundaries = List.copyOf(boundaries);
    }
}
