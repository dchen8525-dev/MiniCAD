package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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
public final class StepValidationInstance extends AbstractStepEntity {
    private final StepEntity validationDefinition;
    private final StepEntity validationTarget;
    private final boolean validationResult;
    private final List<String> validationIssues;
    private final String validationStatus;

    public StepValidationInstance(int id, String name, StepEntity validationDefinition, StepEntity validationTarget, boolean validationResult, List<String> validationIssues, String validationStatus) {
        super(id, name);
        this.validationDefinition = validationDefinition;
        this.validationTarget = validationTarget;
        this.validationResult = validationResult;
        this.validationIssues = validationIssues == null ? null : java.util.List.copyOf(validationIssues);
        this.validationStatus = validationStatus;
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
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("validationDefinition", validationDefinition);
        state.put("validationTarget", validationTarget);
        state.put("validationResult", validationResult);
        state.put("validationIssues", validationIssues);
        state.put("validationStatus", validationStatus);
        return state;
    }
}
