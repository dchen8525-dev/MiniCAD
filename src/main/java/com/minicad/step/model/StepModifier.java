package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Resolved MODIFIER.
 */
public final class StepModifier extends AbstractStepEntity {
    private final String modifierValue;

    public StepModifier(int id, String name, String modifierValue) {
        super(id, name);
        this.modifierValue = modifierValue;
    }

    public String getModifierValue() {
        return modifierValue;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("modifierValue", modifierValue);
        return state;
    }
}
