package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Minimal COLOUR_SPECIFICATION.
 *
 * @param id step id
 * @param name colour name
 */
public final class StepColourSpecification extends AbstractStepEntity {
    public StepColourSpecification(int id, String name) {
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
