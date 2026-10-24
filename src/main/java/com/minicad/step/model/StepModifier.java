package com.minicad.step.model;

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
        return componentsOf(
                "id", getId(), "name", getName(), "modifierValue", modifierValue);
    }
}
