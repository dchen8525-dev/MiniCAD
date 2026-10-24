package com.minicad.step.model;

import java.util.Map;

/**
 * Resolved DRAUGHTING_PRE_DEFINED_TERMINATOR_SYMBOL.
 */
public final class StepDraughtingPreDefinedTerminatorSymbol extends AbstractStepEntity {
    private final String identifier;

    public StepDraughtingPreDefinedTerminatorSymbol(int id, String name, String identifier) {
        super(id, name);
        this.identifier = identifier;
    }

    public String getIdentifier() {
        return identifier;
    }

    @Override
    protected Map<String, Object> components() {
        return componentsOf(
                "id", getId(), "name", getName(), "identifier", identifier);
    }
}
