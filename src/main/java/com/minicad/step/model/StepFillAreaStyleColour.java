package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Minimal fill area style colour.
 *
 * @param id STEP instance id
 * @param name style name
 * @param colour referenced colour
 */
public final class StepFillAreaStyleColour extends AbstractStepEntity {
    private final StepEntity colour;

    public StepFillAreaStyleColour(int id, String name, StepEntity colour) {
        super(id, name);
        this.colour = colour;
    }

    public StepEntity getColour() {
        return colour;
    }

    // Record-style accessor
    public StepEntity colour() {
        return colour;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("colour", colour);
        return state;
    }
}
