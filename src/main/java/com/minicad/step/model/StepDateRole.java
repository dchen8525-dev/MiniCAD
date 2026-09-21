package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Minimal DATE_ROLE metadata.
 *
 * @param id STEP instance id
 * @param name role label
 */
public final class StepDateRole extends AbstractStepEntity {
    public StepDateRole(int id, String name) {
        super(id, name);
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        return state;
    }
}
