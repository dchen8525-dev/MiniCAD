package com.minicad.step.model.validation;

import com.minicad.step.model.core.base.StepEntity;
import java.util.List;
import java.util.Objects;

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
public final class StepDesignValidation implements StepEntity {
    private final int id;
    private final String name;
    private final String validationType;
    private final StepEntity validationCriteria;
    private final List<StepEntity> validationResults;
    private final String validationStatus;
    private final StepEntity validationDate;
    private final StepEntity validationReport;

    public StepDesignValidation(int id, String name, String validationType, StepEntity validationCriteria, List<StepEntity> validationResults, String validationStatus, StepEntity validationDate, StepEntity validationReport) {
        this.id = id;
        this.name = name;
        this.validationType = validationType;
        this.validationCriteria = validationCriteria;
        this.validationResults = validationResults == null ? null : java.util.List.copyOf(validationResults);
        this.validationStatus = validationStatus;
        this.validationDate = validationDate;
        this.validationReport = validationReport;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepDesignValidation that = (StepDesignValidation) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(validationType, that.validationType) && Objects.equals(validationCriteria, that.validationCriteria) && Objects.equals(validationResults, that.validationResults) && Objects.equals(validationStatus, that.validationStatus) && Objects.equals(validationDate, that.validationDate) && Objects.equals(validationReport, that.validationReport);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, validationType, validationCriteria, validationResults, validationStatus, validationDate, validationReport);
    }

    @Override
    public String toString() {
        return "StepDesignValidation{" + "id=" + id + "name=" + name + "validationType=" + validationType + "validationCriteria=" + validationCriteria + "validationResults=" + validationResults + "validationStatus=" + validationStatus + "validationDate=" + validationDate + "validationReport=" + validationReport + "}";
    }
}