package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Minimal PRE_DEFINED_SYMBOL.
 *
 * @param id step id
 * @param name predefined symbol name
 */
public final class StepPreDefinedSymbol extends AbstractStepEntity {
    public StepPreDefinedSymbol(int id, String name) {
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
