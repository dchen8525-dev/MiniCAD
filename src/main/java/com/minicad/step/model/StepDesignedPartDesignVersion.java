package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Resolved DESIGNED_PART_DESIGN_VERSION.
 * A design version associated with a designed part.
 *
 * @param id STEP instance id
 * @param name part name
 * @param description part description
 * @param frameOfReference product context
 */
public final class StepDesignedPartDesignVersion extends AbstractStepEntity {
    private final String description;
    private final StepEntity frameOfReference;

    public StepDesignedPartDesignVersion(int id, String name, String description, StepEntity frameOfReference) {
        super(id, name);
        this.description = description;
        this.frameOfReference = frameOfReference;
    }

    public String getDescription() {
        return description;
    }

    public StepEntity getFrameOfReference() {
        return frameOfReference;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("description", description);
        state.put("frameOfReference", frameOfReference);
        return state;
    }
}
