package com.minicad.step.model.validation;

import com.minicad.step.model.base.StepEntity;
import java.util.List;
import java.util.Objects;

/**
 * Resolved VALIDATION_INSTANCE.
 * A validation instance entity.
 *
 * @param id STEP instance id
 * @param name validation instance name
 * @param validationDefinition validation variance definition reference
 * @param validationTarget validation variance target reference
 * @param validationResult validation variance result (passed/failed)
 * @param validationIssues validation variance issues found
 * @param validationStatus validation variance status
 */
/**
 * Resolved VALIDATION_INSTANCE.
 * A validation instance entity.
 *
 * @param id STEP instance id
 * @param name validation instance name
 * @param validationDefinition validation variance definition reference
 * @param validationTarget validation variance target reference
 * @param validationResult validation variance result (passed/failed)
 * @param validationIssues validation variance issues found
 * @param validationStatus validation variance status
 */
public final class StepValidationInstance implements StepEntity {
    private final int id;
    private final String name;
    private final StepEntity validationDefinition;
    private final StepEntity validationTarget;
    private final boolean validationResult;
    private final List<String> validationIssues;
    private final String validationStatus;

    public StepValidationInstance(int id, String name, StepEntity validationDefinition, StepEntity validationTarget, boolean validationResult, List<String> validationIssues, String validationStatus) {
        this.id = id;
        this.name = name;
        this.validationDefinition = validationDefinition;
        this.validationTarget = validationTarget;
        this.validationResult = validationResult;
        this.validationIssues = validationIssues == null ? null : java.util.List.copyOf(validationIssues);
        this.validationStatus = validationStatus;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public StepEntity getValidationDefinition() {
        return validationDefinition;
    }

    public StepEntity getValidationTarget() {
        return validationTarget;
    }

    public boolean isValidationResult() {
        return validationResult;
    }

    public List<String> getValidationIssues() {
        return validationIssues;
    }

    public String getValidationStatus() {
        return validationStatus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepValidationInstance that = (StepValidationInstance) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(validationDefinition, that.validationDefinition) && Objects.equals(validationTarget, that.validationTarget) && validationResult == that.validationResult && Objects.equals(validationIssues, that.validationIssues) && Objects.equals(validationStatus, that.validationStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, validationDefinition, validationTarget, validationResult, validationIssues, validationStatus);
    }

    @Override
    public String toString() {
        return "StepValidationInstance{" + "id=" + id + "name=" + name + "validationDefinition=" + validationDefinition + "validationTarget=" + validationTarget + "validationResult=" + validationResult + "validationIssues=" + validationIssues + "validationStatus=" + validationStatus + "}";
    }
}