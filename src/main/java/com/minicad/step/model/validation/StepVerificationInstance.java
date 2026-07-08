package com.minicad.step.model.validation;

import com.minicad.step.model.core.base.StepEntity;
import java.util.List;
import java.util.Objects;

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
public final class StepVerificationInstance implements StepEntity {
    private final int id;
    private final String name;
    private final StepEntity verificationDefinition;
    private final StepEntity verificationTarget;
    private final boolean verificationResult;
    private final List<Double> verificationMeasurements;
    private final String verificationStatus;

    public StepVerificationInstance(int id, String name, StepEntity verificationDefinition, StepEntity verificationTarget, boolean verificationResult, List<Double> verificationMeasurements, String verificationStatus) {
        this.id = id;
        this.name = name;
        this.verificationDefinition = verificationDefinition;
        this.verificationTarget = verificationTarget;
        this.verificationResult = verificationResult;
        this.verificationMeasurements = verificationMeasurements == null ? null : java.util.List.copyOf(verificationMeasurements);
        this.verificationStatus = verificationStatus;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepVerificationInstance that = (StepVerificationInstance) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(verificationDefinition, that.verificationDefinition) && Objects.equals(verificationTarget, that.verificationTarget) && verificationResult == that.verificationResult && Objects.equals(verificationMeasurements, that.verificationMeasurements) && Objects.equals(verificationStatus, that.verificationStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, verificationDefinition, verificationTarget, verificationResult, verificationMeasurements, verificationStatus);
    }

    @Override
    public String toString() {
        return "StepVerificationInstance{" + "id=" + id + "name=" + name + "verificationDefinition=" + verificationDefinition + "verificationTarget=" + verificationTarget + "verificationResult=" + verificationResult + "verificationMeasurements=" + verificationMeasurements + "verificationStatus=" + verificationStatus + "}";
    }
}