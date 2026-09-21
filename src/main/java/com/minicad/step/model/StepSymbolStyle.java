package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Minimal SYMBOL_STYLE.
 *
 * @param id STEP instance id
 * @param name style name
 * @param styleOfSymbol symbol style payload
 */
public final class StepSymbolStyle extends AbstractStepEntity {
    private final StepEntity styleOfSymbol;

    public StepSymbolStyle(int id, String name, StepEntity styleOfSymbol) {
        super(id, name);
        this.styleOfSymbol = styleOfSymbol;
    }

    public StepEntity getStyleOfSymbol() {
        return styleOfSymbol;
    }

    // Record-style accessor
    public StepEntity styleOfSymbol() {
        return styleOfSymbol;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("styleOfSymbol", styleOfSymbol);
        return state;
    }
}
