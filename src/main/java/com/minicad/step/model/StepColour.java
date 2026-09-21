package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Minimal COLOUR marker.
 *
 * @param id step id
 */
public final class StepColour extends AbstractStepEntity {
    public StepColour(int id) {
        super(id, "");
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        return state;
    }
}
