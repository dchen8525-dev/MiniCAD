package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Minimal SECURITY_CLASSIFICATION_LEVEL metadata.
 *
 * @param id STEP instance id
 * @param name level label
 */
public final class StepSecurityClassificationLevel extends AbstractStepEntity {
    public StepSecurityClassificationLevel(int id, String name) {
        super(id, name);
    }

    // Record-style accessor
    public String securityLevel() {
        return getName();
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        return state;
    }
}
