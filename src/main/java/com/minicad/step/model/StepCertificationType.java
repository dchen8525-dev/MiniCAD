package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Minimal CERTIFICATION_TYPE metadata.
 *
 * @param id STEP instance id
 * @param description type description
 */
public final class StepCertificationType extends AbstractStepEntity {
    private final String description;

    public StepCertificationType(int id, String description) {
        super(id, "");
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public String getName() {
        return description != null ? description : "";
    }

    // Record-style accessor
    public String kind() {
        return description;
    }

    public String description() {
        return description;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("description", description);
        return state;
    }
}
