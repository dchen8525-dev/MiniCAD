package com.minicad.step.model.validation;

import com.minicad.step.model.core.base.StepEntity;
import java.util.List;
import java.util.Objects;

/**
 * Resolved ACCEPTANCE_TEST_RESULT.
 * An acceptance test result entity.
 *
 * @param id STEP instance id
 * @param name result name
 * @varianceSystem tested variance system
 * @varianceCriteria acceptance variance criteria
 * @varianceTests acceptance variance test cases
 * @varianceOutcome acceptance variance outcome
 * @varianceSignoff signoff variance reference
 * @varianceStatus result variance status
 */
/**
 * Resolved ACCEPTANCE_TEST_RESULT.
 * An acceptance test result entity.
 *
 * @param id STEP instance id
 * @param name result name
 * @varianceSystem tested variance system
 * @varianceCriteria acceptance variance criteria
 * @varianceTests acceptance variance test cases
 * @varianceOutcome acceptance variance outcome
 * @varianceSignoff signoff variance reference
 * @varianceStatus result variance status
 */
public final class StepAcceptanceTestResult implements StepEntity {
    private final int id;
    private final String name;
    private final StepEntity varianceSystem;
    private final StepEntity varianceCriteria;
    private final List<StepEntity> varianceTests;
    private final String varianceOutcome;
    private final StepEntity varianceSignoff;
    private final String varianceStatus;

    public StepAcceptanceTestResult(int id, String name, StepEntity varianceSystem, StepEntity varianceCriteria, List<StepEntity> varianceTests, String varianceOutcome, StepEntity varianceSignoff, String varianceStatus) {
        this.id = id;
        this.name = name;
        this.varianceSystem = varianceSystem;
        this.varianceCriteria = varianceCriteria;
        this.varianceTests = varianceTests == null ? null : java.util.List.copyOf(varianceTests);
        this.varianceOutcome = varianceOutcome;
        this.varianceSignoff = varianceSignoff;
        this.varianceStatus = varianceStatus;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public StepEntity getVarianceSystem() {
        return varianceSystem;
    }

    public StepEntity getVarianceCriteria() {
        return varianceCriteria;
    }

    public List<StepEntity> getVarianceTests() {
        return varianceTests;
    }

    public String getVarianceOutcome() {
        return varianceOutcome;
    }

    public StepEntity getVarianceSignoff() {
        return varianceSignoff;
    }

    public String getVarianceStatus() {
        return varianceStatus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepAcceptanceTestResult that = (StepAcceptanceTestResult) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(varianceSystem, that.varianceSystem) && Objects.equals(varianceCriteria, that.varianceCriteria) && Objects.equals(varianceTests, that.varianceTests) && Objects.equals(varianceOutcome, that.varianceOutcome) && Objects.equals(varianceSignoff, that.varianceSignoff) && Objects.equals(varianceStatus, that.varianceStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, varianceSystem, varianceCriteria, varianceTests, varianceOutcome, varianceSignoff, varianceStatus);
    }

    @Override
    public String toString() {
        return "StepAcceptanceTestResult{" + "id=" + id + "name=" + name + "varianceSystem=" + varianceSystem + "varianceCriteria=" + varianceCriteria + "varianceTests=" + varianceTests + "varianceOutcome=" + varianceOutcome + "varianceSignoff=" + varianceSignoff + "varianceStatus=" + varianceStatus + "}";
    }
}