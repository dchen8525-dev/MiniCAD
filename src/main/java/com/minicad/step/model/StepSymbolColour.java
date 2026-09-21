package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Minimal SYMBOL_COLOUR.
 *
 * @param id STEP instance id
 * @param colour referenced colour
 */
public final class StepSymbolColour extends AbstractStepEntity {
    private final StepEntity colour;

    public StepSymbolColour(int id, StepEntity colour) {
        super(id, "");
        this.colour = colour;
    }

    public StepEntity getColour() {
        return colour;
    }

    // Record-style accessors
    public int id() { return getId(); }
    public String name() { return getName(); }
    public StepEntity colour() { return getColour(); }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("colour", colour);
        return state;
    }
}
