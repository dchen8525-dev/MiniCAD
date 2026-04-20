package com.minicad.step.model.validation;

import com.minicad.step.model.base.StepEntity;
import java.util.List;

/**
 * Resolved ERROR_DEFINITION.
 * An error definition entity.
 *
 * @param id STEP instance id
 * @param name error name
 * @param errorType error variance type
 * @param errorCode error variance code
 * @param errorDescription error variance description
 * @param errorSeverity error variance severity level
 * @param errorStatus error variance status
 */
public record StepErrorDefinition(
    int id,
    String name,
    String errorType,
    String errorCode,
    String errorDescription,
    int errorSeverity,
    String errorStatus) implements StepEntity {
}