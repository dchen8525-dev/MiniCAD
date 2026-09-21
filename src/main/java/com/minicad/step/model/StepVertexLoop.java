package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Resolved VERTEX_LOOP.
 *
 * @param id step id
 * @param name step label
 * @param loopVertex referenced single vertex
 */
public final class StepVertexLoop extends AbstractStepEntity implements StepLoop {
    private final StepVertexPoint loopVertex;

    public StepVertexLoop(int id, String name, StepVertexPoint loopVertex) {
        super(id, name);
        this.loopVertex = loopVertex;
    }

    public StepVertexPoint getLoopVertex() {
        return loopVertex;
    }

    // Record-style accessor
    public StepVertexPoint loopVertex() {
        return loopVertex;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("loopVertex", loopVertex);
        return state;
    }
}
