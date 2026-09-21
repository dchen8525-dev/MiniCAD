package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Minimal PRE_DEFINED_GEOMETRICAL_TOLERANCE_SYMBOL.
 *
 * @param id step id
 * @param name predefined geometrical tolerance symbol name
 */
public final class StepPreDefinedGeometricalToleranceSymbol extends AbstractStepEntity {
    public StepPreDefinedGeometricalToleranceSymbol(int id, String name) {
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
