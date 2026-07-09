package com.minicad.step.model;

import com.minicad.step.model.core.base.StepEntity;
import java.util.List;
import java.util.Objects;

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
public final class StepDesignVerification implements StepEntity {
    private final int id;
    private final String name;
    private final String verificationType;
    private final StepEntity verificationCriteria;
    private final List<StepEntity> verificationResults;
    private final String verificationStatus;
    private final String verificationMethod;
    private final StepEntity verificationEvidence;

    public StepDesignVerification(int id, String name, String verificationType, StepEntity verificationCriteria, List<StepEntity> verificationResults, String verificationStatus, String verificationMethod, StepEntity verificationEvidence) {
        this.id = id;
        this.name = name;
        this.verificationType = verificationType;
        this.verificationCriteria = verificationCriteria;
        this.verificationResults = verificationResults == null ? null : java.util.List.copyOf(verificationResults);
        this.verificationStatus = verificationStatus;
        this.verificationMethod = verificationMethod;
        this.verificationEvidence = verificationEvidence;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepDesignVerification that = (StepDesignVerification) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(verificationType, that.verificationType) && Objects.equals(verificationCriteria, that.verificationCriteria) && Objects.equals(verificationResults, that.verificationResults) && Objects.equals(verificationStatus, that.verificationStatus) && Objects.equals(verificationMethod, that.verificationMethod) && Objects.equals(verificationEvidence, that.verificationEvidence);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, verificationType, verificationCriteria, verificationResults, verificationStatus, verificationMethod, verificationEvidence);
    }

    @Override
    public String toString() {
        return "StepDesignVerification{" + "id=" + id + "name=" + name + "verificationType=" + verificationType + "verificationCriteria=" + verificationCriteria + "verificationResults=" + verificationResults + "verificationStatus=" + verificationStatus + "verificationMethod=" + verificationMethod + "verificationEvidence=" + verificationEvidence + "}";
    }
}