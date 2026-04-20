package com.minicad.step.model.validation;

import com.minicad.step.model.base.StepEntity;
import java.util.List;

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
public record StepValidationDefinition(
    int id,
    String name,
    String validationType,
    List<String> validationCriteria,
    List<StepEntity> validationRules,
    String validationScope,
    String validationStatus) implements StepEntity {
}