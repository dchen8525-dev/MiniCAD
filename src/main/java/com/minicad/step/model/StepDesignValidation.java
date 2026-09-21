package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Resolved DESIGN_VALIDATION.
 * A design validation entity.
 *
 * @param id STEP instance id
 * @param name validation name
 * @param validationType validation type (analysis, test, inspection)
 * @param validationCriteria validation criteria reference
 * @param validationResults validation results
 * @param validationStatus validation status (passed, failed, pending)
 * @param validationDate validation execution date
 * @param validationReport validation report reference
 */
public final class StepDesignValidation extends AbstractStepEntity {
    private final String validationType;
    private final StepEntity validationCriteria;
    private final List<StepEntity> validationResults;
    private final String validationStatus;
    private final StepEntity validationDate;
    private final StepEntity validationReport;

    public StepDesignValidation(int id, String name, String validationType, StepEntity validationCriteria, List<StepEntity> validationResults, String validationStatus, StepEntity validationDate, StepEntity validationReport) {
        super(id, name);
        this.validationType = validationType;
        this.validationCriteria = validationCriteria;
        this.validationResults = validationResults == null ? null : java.util.List.copyOf(validationResults);
        this.validationStatus = validationStatus;
        this.validationDate = validationDate;
        this.validationReport = validationReport;
    }

    public String getValidationType() {
        return validationType;
    }

    public StepEntity getValidationCriteria() {
        return validationCriteria;
    }

    public List<StepEntity> getValidationResults() {
        return validationResults;
    }

    public String getValidationStatus() {
        return validationStatus;
    }

    public StepEntity getValidationDate() {
        return validationDate;
    }

    public StepEntity getValidationReport() {
        return validationReport;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("validationType", validationType);
        state.put("validationCriteria", validationCriteria);
        state.put("validationResults", validationResults);
        state.put("validationStatus", validationStatus);
        state.put("validationDate", validationDate);
        state.put("validationReport", validationReport);
        return state;
    }
}
