package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Minimal EXTERNAL_SOURCE metadata.
 *
 * @param id STEP instance id
 * @param sourceId external source identifier
 */
public final class StepExternalSource extends AbstractStepEntity {
    private final String sourceId;

    public StepExternalSource(int id, String sourceId) {
        super(id, "");
        this.sourceId = sourceId;
    }

    public String getSourceId() {
        return sourceId;
    }

    public String getName() {
        return sourceId != null ? sourceId : "";
    }

    // Record-style accessor
    public String sourceId() {
        return sourceId;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("sourceId", sourceId);
        return state;
    }
}
