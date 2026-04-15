package com.minicad.step.model;

import java.util.List;

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
public record StepValidationInstance(
    int id,
    String name,
    StepEntity validationDefinition,
    StepEntity validationTarget,
    boolean validationResult,
    List<String> validationIssues,
    String validationStatus) implements StepEntity {
}