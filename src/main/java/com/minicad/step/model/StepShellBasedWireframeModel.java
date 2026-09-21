package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Resolved SHELL_BASED_WIREFRAME_MODEL.
 *
 * @param id STEP id
 * @param name STEP label
 * @param boundaries referenced vertex or wire shells
 */
public final class StepShellBasedWireframeModel extends AbstractStepEntity {
    private final List<StepEntity> boundaries;

    public StepShellBasedWireframeModel(int id, String name, List<StepEntity> boundaries) {
        super(id, name);
        this.boundaries = boundaries == null ? null : java.util.List.copyOf(boundaries);
    }

    public List<StepEntity> getBoundaries() {
        return boundaries;
    }

    // Record-style accessor
    public List<StepEntity> boundaries() {
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
