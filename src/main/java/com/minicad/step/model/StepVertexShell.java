package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Resolved VERTEX_SHELL.
 *
 * @param id STEP id
 * @param name STEP label
 * @param extent defining vertex loop
 */
public final class StepVertexShell extends AbstractStepEntity {
    private final StepVertexLoop extent;

    public StepVertexShell(int id, String name, StepVertexLoop extent) {
        super(id, name);
        this.extent = extent;
    }

    public StepVertexLoop getExtent() {
        return extent;
    }

    // Record-style accessor
    public StepVertexLoop extent() {
        return extent;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("extent", extent);
        return state;
    }
}
