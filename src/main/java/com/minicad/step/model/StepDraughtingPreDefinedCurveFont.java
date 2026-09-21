package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Minimal DRAUGHTING_PRE_DEFINED_CURVE_FONT.
 *
 * @param id step id
 * @param name predefined font name
 */
public final class StepDraughtingPreDefinedCurveFont extends AbstractStepEntity {
    public StepDraughtingPreDefinedCurveFont(int id, String name) {
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
