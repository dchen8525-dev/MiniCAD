package com.minicad.step.model;

import java.util.Map;

/**
 * Resolved PRE_DEFINED_SURFACE_STYLE.
 */
public final class StepPreDefinedSurfaceStyle extends AbstractStepEntity {
    private final String identifier;

    public StepPreDefinedSurfaceStyle(int id, String name, String identifier) {
        super(id, name);
        this.identifier = identifier;
    }

    public String getIdentifier() {
        return identifier;
    }

    // Record-style accessors
    public String name() {
        return getName();
    }

    public String identifier() {
        return identifier;
    }

    @Override
    protected Map<String, Object> components() {
        return componentsOf(
                "id", getId(), "name", getName(), "identifier", identifier);
    }
}
