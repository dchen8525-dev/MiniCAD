package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Minimal DRAUGHTING_PRE_DEFINED_TEXT_FONT.
 *
 * @param id step id
 * @param name predefined draughting text font name
 */
public final class StepDraughtingPreDefinedTextFont extends AbstractStepEntity {
    public StepDraughtingPreDefinedTextFont(int id, String name) {
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
