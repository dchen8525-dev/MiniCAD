package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Resolved VERIFICATION_DEFINITION.
 * A verification definition entity.
 *
 * @param id STEP instance id
 * @param name verification name
 * @param verificationType verification variance type
 * @param verificationMethod verification variance method
 * @param verificationCriteria verification variance criteria
 * @param verificationTolerance verification variance tolerance
 * @param verificationStatus verification variance status
 */
public final class StepVerificationDefinition extends AbstractStepEntity {
    private final String verificationType;
    private final String verificationMethod;
    private final List<String> verificationCriteria;
    private final double verificationTolerance;
    private final String verificationStatus;

    public StepVerificationDefinition(int id, String name, String verificationType, String verificationMethod, List<String> verificationCriteria, double verificationTolerance, String verificationStatus) {
        super(id, name);
        this.verificationType = verificationType;
        this.verificationMethod = verificationMethod;
        this.verificationCriteria = verificationCriteria == null ? null : java.util.List.copyOf(verificationCriteria);
        this.verificationTolerance = verificationTolerance;
        this.verificationStatus = verificationStatus;
    }

    public String getVerificationType() {
        return verificationType;
    }

    public String getVerificationMethod() {
        return verificationMethod;
    }

    public List<String> getVerificationCriteria() {
        return verificationCriteria;
    }

    public double getVerificationTolerance() {
        return verificationTolerance;
    }

    public String getVerificationStatus() {
        return verificationStatus;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("verificationType", verificationType);
        state.put("verificationMethod", verificationMethod);
        state.put("verificationCriteria", verificationCriteria);
        state.put("verificationTolerance", verificationTolerance);
        state.put("verificationStatus", verificationStatus);
        return state;
    }
}
