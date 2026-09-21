package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Resolved EDGE_BASED_WIREFRAME_MODEL.
 *
 * @param id STEP id
 * @param name STEP label
 * @param boundaries connected edge sets
 */
public final class StepEdgeBasedWireframeModel extends AbstractStepEntity {
    private final List<StepConnectedEdgeSet> boundaries;

    public StepEdgeBasedWireframeModel(int id, String name, List<StepConnectedEdgeSet> boundaries) {
        super(id, name);
        this.boundaries = boundaries == null ? null : java.util.List.copyOf(boundaries);
    }

    public List<StepConnectedEdgeSet> getBoundaries() {
        return boundaries;
    }

    // Record-style accessor
    public List<StepConnectedEdgeSet> boundaries() {
        return boundaries;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("boundaries", boundaries);
        return state;
    }
}
