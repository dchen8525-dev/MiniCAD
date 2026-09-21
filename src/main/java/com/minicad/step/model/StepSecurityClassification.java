package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Minimal SECURITY_CLASSIFICATION metadata.
 *
 * @param id STEP instance id
 * @param name classification name
 * @param purpose classification purpose
 * @param securityLevel classification level
 */
public final class StepSecurityClassification extends AbstractStepEntity {
    private final String purpose;
    private final StepSecurityClassificationLevel securityLevel;

    public StepSecurityClassification(int id, String name, String purpose, StepSecurityClassificationLevel securityLevel) {
        super(id, name);
        this.purpose = purpose;
        this.securityLevel = securityLevel;
    }

    public String getPurpose() {
        return purpose;
    }

    public StepSecurityClassificationLevel getSecurityLevel() {
        return securityLevel;
    }

    // Record-style accessor
    public StepSecurityClassificationLevel securityLevel() {
        return securityLevel;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("purpose", purpose);
        state.put("securityLevel", securityLevel);
        return state;
    }
}
