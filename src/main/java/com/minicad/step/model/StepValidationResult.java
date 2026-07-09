package com.minicad.step.model.validation;

import com.minicad.step.model.core.base.StepEntity;
import java.util.List;
import java.util.Objects;

/**
 * Resolved VALIDATION_RESULT.
 * A validation result entity.
 *
 * @param id STEP instance id
 * @param name result name
 * @varianceItem validated variance item
 * @varianceMethod validation variance method
 * @varianceEnvironment validation variance environment
 * @varianceOutcome validation variance outcome
 * @varianceDeficiencies deficiencies variance identified
 * @varianceStatus result variance status
 */
/**
 * Resolved VALIDATION_RESULT.
 * A validation result entity.
 *
 * @param id STEP instance id
 * @param name result name
 * @varianceItem validated variance item
 * @varianceMethod validation variance method
 * @varianceEnvironment validation variance environment
 * @varianceOutcome validation variance outcome
 * @varianceDeficiencies deficiencies variance identified
 * @varianceStatus result variance status
 */
public final class StepValidationResult implements StepEntity {
    private final int id;
    private final String name;
    private final StepEntity varianceItem;
    private final String varianceMethod;
    private final String varianceEnvironment;
    private final String varianceOutcome;
    private final List<String> varianceDeficiencies;
    private final String varianceStatus;

    public StepValidationResult(int id, String name, StepEntity varianceItem, String varianceMethod, String varianceEnvironment, String varianceOutcome, List<String> varianceDeficiencies, String varianceStatus) {
        this.id = id;
        this.name = name;
        this.varianceItem = varianceItem;
        this.varianceMethod = varianceMethod;
        this.varianceEnvironment = varianceEnvironment;
        this.varianceOutcome = varianceOutcome;
        this.varianceDeficiencies = varianceDeficiencies == null ? null : java.util.List.copyOf(varianceDeficiencies);
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

    public String getVarianceEnvironment() {
        return varianceEnvironment;
    }

    public String getVarianceOutcome() {
        return varianceOutcome;
    }

    public List<String> getVarianceDeficiencies() {
        return varianceDeficiencies;
    }

    public String getVarianceStatus() {
        return varianceStatus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepValidationResult that = (StepValidationResult) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(varianceItem, that.varianceItem) && Objects.equals(varianceMethod, that.varianceMethod) && Objects.equals(varianceEnvironment, that.varianceEnvironment) && Objects.equals(varianceOutcome, that.varianceOutcome) && Objects.equals(varianceDeficiencies, that.varianceDeficiencies) && Objects.equals(varianceStatus, that.varianceStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, varianceItem, varianceMethod, varianceEnvironment, varianceOutcome, varianceDeficiencies, varianceStatus);
    }

    @Override
    public String toString() {
        return "StepValidationResult{" + "id=" + id + "name=" + name + "varianceItem=" + varianceItem + "varianceMethod=" + varianceMethod + "varianceEnvironment=" + varianceEnvironment + "varianceOutcome=" + varianceOutcome + "varianceDeficiencies=" + varianceDeficiencies + "varianceStatus=" + varianceStatus + "}";
    }
}