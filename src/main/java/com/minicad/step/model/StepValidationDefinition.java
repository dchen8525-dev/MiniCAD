package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Resolved VALIDATION_DEFINITION.
 * A validation definition entity.
 *
 * @param id STEP instance id
 * @param name validation name
 * @param validationType validation variance type
 * @param validationCriteria validation variance criteria
 * @param validationRules validation variance rules
 * @param validationScope validation variance scope
 * @param validationStatus validation variance status
 */
public final class StepValidationDefinition extends AbstractStepEntity {
    private final String validationType;
    private final List<String> validationCriteria;
    private final List<StepEntity> validationRules;
    private final String validationScope;
    private final String validationStatus;

    public StepValidationDefinition(int id, String name, String validationType, List<String> validationCriteria, List<StepEntity> validationRules, String validationScope, String validationStatus) {
        super(id, name);
        this.validationType = validationType;
        this.validationCriteria = validationCriteria == null ? null : java.util.List.copyOf(validationCriteria);
        this.validationRules = validationRules == null ? null : java.util.List.copyOf(validationRules);
        this.validationScope = validationScope;
        this.validationStatus = validationStatus;
    }

    public String getValidationType() {
        return validationType;
    }

    public List<String> getValidationCriteria() {
        return validationCriteria;
    }

    public List<StepEntity> getValidationRules() {
        return validationRules;
    }

    public String getValidationScope() {
        return validationScope;
    }

    public String getValidationStatus() {
        return validationStatus;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("validationType", validationType);
        state.put("validationCriteria", validationCriteria);
        state.put("validationRules", validationRules);
        state.put("validationScope", validationScope);
        state.put("validationStatus", validationStatus);
        return state;
    }
}
