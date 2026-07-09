package com.minicad.step.model;

import com.minicad.step.model.StepEntity;
import java.util.List;
import java.util.Objects;

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
public final class StepVerificationDefinition implements StepEntity {
    private final int id;
    private final String name;
    private final String verificationType;
    private final String verificationMethod;
    private final List<String> verificationCriteria;
    private final double verificationTolerance;
    private final String verificationStatus;

    public StepVerificationDefinition(int id, String name, String verificationType, String verificationMethod, List<String> verificationCriteria, double verificationTolerance, String verificationStatus) {
        this.id = id;
        this.name = name;
        this.verificationType = verificationType;
        this.verificationMethod = verificationMethod;
        this.verificationCriteria = verificationCriteria == null ? null : java.util.List.copyOf(verificationCriteria);
        this.verificationTolerance = verificationTolerance;
        this.verificationStatus = verificationStatus;
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepVerificationDefinition that = (StepVerificationDefinition) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(verificationType, that.verificationType) && Objects.equals(verificationMethod, that.verificationMethod) && Objects.equals(verificationCriteria, that.verificationCriteria) && verificationTolerance == that.verificationTolerance && Objects.equals(verificationStatus, that.verificationStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, verificationType, verificationMethod, verificationCriteria, verificationTolerance, verificationStatus);
    }

    @Override
    public String toString() {
        return "StepVerificationDefinition{" + "id=" + id + "name=" + name + "verificationType=" + verificationType + "verificationMethod=" + verificationMethod + "verificationCriteria=" + verificationCriteria + "verificationTolerance=" + verificationTolerance + "verificationStatus=" + verificationStatus + "}";
    }
}