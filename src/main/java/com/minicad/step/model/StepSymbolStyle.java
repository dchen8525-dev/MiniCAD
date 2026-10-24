package com.minicad.step.model;

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
        return componentsOf(
                "id", getId(), "name", getName(), "styleOfSymbol", styleOfSymbol);
    }
}
