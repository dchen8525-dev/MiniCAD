package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Resolved DESIGN_VERIFICATION.
 * A design verification entity.
 *
 * @param id STEP instance id
 * @param name verification name
 * @param verificationType verification type (calculation, simulation, review)
 * @param verificationCriteria verification criteria reference
 * @param verificationResults verification results
 * @param verificationStatus verification status (verified, not verified)
 * @param verificationMethod verification method description
 * @param verificationEvidence verification evidence reference
 */
public final class StepDesignVerification extends AbstractStepEntity {
    private final String verificationType;
    private final StepEntity verificationCriteria;
    private final List<StepEntity> verificationResults;
    private final String verificationStatus;
    private final String verificationMethod;
    private final StepEntity verificationEvidence;

    public StepDesignVerification(int id, String name, String verificationType, StepEntity verificationCriteria, List<StepEntity> verificationResults, String verificationStatus, String verificationMethod, StepEntity verificationEvidence) {
        super(id, name);
        this.verificationType = verificationType;
        this.verificationCriteria = verificationCriteria;
        this.verificationResults = verificationResults == null ? null : java.util.List.copyOf(verificationResults);
        this.verificationStatus = verificationStatus;
        this.verificationMethod = verificationMethod;
        this.verificationEvidence = verificationEvidence;
    }

    public String getVerificationType() {
        return verificationType;
    }

    public StepEntity getVerificationCriteria() {
        return verificationCriteria;
    }

    public List<StepEntity> getVerificationResults() {
        return verificationResults;
    }

    public String getVerificationStatus() {
        return verificationStatus;
    }

    public String getVerificationMethod() {
        return verificationMethod;
    }

    public StepEntity getVerificationEvidence() {
        return verificationEvidence;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("verificationType", verificationType);
        state.put("verificationCriteria", verificationCriteria);
        state.put("verificationResults", verificationResults);
        state.put("verificationStatus", verificationStatus);
        state.put("verificationMethod", verificationMethod);
        state.put("verificationEvidence", verificationEvidence);
        return state;
    }
}
