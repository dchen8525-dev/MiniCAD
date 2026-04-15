package com.minicad.step.model;

import java.util.List;

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
public record StepDesignValidation(
    int id,
    String name,
    String validationType,
    StepEntity validationCriteria,
    List<StepEntity> validationResults,
    String validationStatus,
    StepEntity validationDate,
    StepEntity validationReport) implements StepEntity {
}