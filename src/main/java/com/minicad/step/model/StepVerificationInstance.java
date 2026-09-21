package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Resolved VERIFICATION_INSTANCE.
 * A verification instance entity.
 *
 * @param id STEP instance id
 * @param name verification instance name
 * @param verificationDefinition verification variance definition reference
 * @param verificationTarget verification variance target reference
 * @param verificationResult verification variance result (passed/failed)
 * @param verificationMeasurements verification variance measurements
 * @param verificationStatus verification variance status
 */
public final class StepVerificationInstance extends AbstractStepEntity {
    private final StepEntity verificationDefinition;
    private final StepEntity verificationTarget;
    private final boolean verificationResult;
    private final List<Double> verificationMeasurements;
    private final String verificationStatus;

    public StepVerificationInstance(int id, String name, StepEntity verificationDefinition, StepEntity verificationTarget, boolean verificationResult, List<Double> verificationMeasurements, String verificationStatus) {
        super(id, name);
        this.verificationDefinition = verificationDefinition;
        this.verificationTarget = verificationTarget;
        this.verificationResult = verificationResult;
        this.verificationMeasurements = verificationMeasurements == null ? null : java.util.List.copyOf(verificationMeasurements);
        this.verificationStatus = verificationStatus;
    }

    public StepEntity getVerificationDefinition() {
        return verificationDefinition;
    }

    public StepEntity getVerificationTarget() {
        return verificationTarget;
    }

    public boolean isVerificationResult() {
        return verificationResult;
    }

    public List<Double> getVerificationMeasurements() {
        return verificationMeasurements;
    }

    public String getVerificationStatus() {
        return verificationStatus;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("verificationDefinition", verificationDefinition);
        state.put("verificationTarget", verificationTarget);
        state.put("verificationResult", verificationResult);
        state.put("verificationMeasurements", verificationMeasurements);
        state.put("verificationStatus", verificationStatus);
        return state;
    }
}
