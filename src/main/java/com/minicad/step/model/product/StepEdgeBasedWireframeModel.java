package com.minicad.step.model.product;

import com.minicad.step.model.base.StepEntity;
import java.util.List;

import com.minicad.step.model.topology.StepConnectedEdgeSet;

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
