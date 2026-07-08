package com.minicad.step.model.validation;

import com.minicad.step.model.core.base.StepEntity;
import java.util.List;
import java.util.Objects;

/**
 * Resolved VERIFICATION_RESULT.
 * A verification result entity.
 *
 * @param id STEP instance id
 * @param name result name
 * @varianceItem verified variance item
 * @varianceMethod verification variance method
 * @varianceCriteria verification variance criteria
 * @varianceOutcome verification variance outcome (pass/fail)
 * @varianceEvidence evidence variance reference
 * @varianceStatus result variance status
 */
/**
 * Resolved VERIFICATION_RESULT.
 * A verification result entity.
 *
 * @param id STEP instance id
 * @param name result name
 * @varianceItem verified variance item
 * @varianceMethod verification variance method
 * @varianceCriteria verification variance criteria
 * @varianceOutcome verification variance outcome (pass/fail)
 * @varianceEvidence evidence variance reference
 * @varianceStatus result variance status
 */
public final class StepVerificationResult implements StepEntity {
    private final int id;
    private final String name;
    private final StepEntity varianceItem;
    private final String varianceMethod;
    private final StepEntity varianceCriteria;
    private final String varianceOutcome;
    private final StepEntity varianceEvidence;
    private final String varianceStatus;

    public StepVerificationResult(int id, String name, StepEntity varianceItem, String varianceMethod, StepEntity varianceCriteria, String varianceOutcome, StepEntity varianceEvidence, String varianceStatus) {
        this.id = id;
        this.name = name;
        this.varianceItem = varianceItem;
        this.varianceMethod = varianceMethod;
        this.varianceCriteria = varianceCriteria;
        this.varianceOutcome = varianceOutcome;
        this.varianceEvidence = varianceEvidence;
        this.varianceStatus = varianceStatus;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public StepEntity getVarianceItem() {
        return varianceItem;
    }

    public String getVarianceMethod() {
        return varianceMethod;
    }

    public StepEntity getVarianceCriteria() {
        return varianceCriteria;
    }

    public String getVarianceOutcome() {
        return varianceOutcome;
    }

    public StepEntity getVarianceEvidence() {
        return varianceEvidence;
    }

    public String getVarianceStatus() {
        return varianceStatus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepVerificationResult that = (StepVerificationResult) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(varianceItem, that.varianceItem) && Objects.equals(varianceMethod, that.varianceMethod) && Objects.equals(varianceCriteria, that.varianceCriteria) && Objects.equals(varianceOutcome, that.varianceOutcome) && Objects.equals(varianceEvidence, that.varianceEvidence) && Objects.equals(varianceStatus, that.varianceStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, varianceItem, varianceMethod, varianceCriteria, varianceOutcome, varianceEvidence, varianceStatus);
    }

    @Override
    public String toString() {
        return "StepVerificationResult{" + "id=" + id + "name=" + name + "varianceItem=" + varianceItem + "varianceMethod=" + varianceMethod + "varianceCriteria=" + varianceCriteria + "varianceOutcome=" + varianceOutcome + "varianceEvidence=" + varianceEvidence + "varianceStatus=" + varianceStatus + "}";
    }
}