package com.minicad.step.model.topology;

import com.minicad.step.model.base.StepEntity;
import java.util.List;

/**
 * Resolved CONNECTED_EDGE_SET.
 *
 * @param id STEP id
 * @param name STEP label
 * @param edges member edges
 */
public record StepConnectedEdgeSet(int id, String name, List<StepEntity> edges) implements StepEntity {

    public StepConnectedEdgeSet {
        edges = List.copyOf(edges);
    }
}
