package com.minicad.step.model;

import java.util.LinkedHashMap;
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
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("externalSource", externalSource);
        return state;
    }
}
