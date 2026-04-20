package com.minicad.step.model.validation;

import com.minicad.step.model.base.StepEntity;
import java.util.List;

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
public record StepValidationResult(
    int id,
    String name,
    StepEntity varianceItem,
    String varianceMethod,
    String varianceEnvironment,
    String varianceOutcome,
    List<String> varianceDeficiencies,
    String varianceStatus) implements StepEntity {
}