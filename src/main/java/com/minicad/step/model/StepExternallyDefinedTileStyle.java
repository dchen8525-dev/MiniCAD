package com.minicad.step.model;

import java.util.Map;

/**
 * Resolved EXTERNALLY_DEFINED_TILE_STYLE.
 */
public final class StepExternallyDefinedTileStyle extends AbstractStepEntity {
    private final StepEntity externalSource;

    public StepExternallyDefinedTileStyle(int id, String name, StepEntity externalSource) {
        super(id, name);
        this.externalSource = externalSource;
    }

    public StepEntity getExternalSource() {
        return externalSource;
    }

    @Override
    protected Map<String, Object> components() {
        return componentsOf(
                "id", getId(), "name", getName(), "externalSource", externalSource);
    }
}
