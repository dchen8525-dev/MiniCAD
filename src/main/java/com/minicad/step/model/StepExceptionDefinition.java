package com.minicad.step.model;

import java.util.List;

/**
 * Resolved EXCEPTION_DEFINITION.
 * An exception definition entity.
 *
 * @param id STEP instance id
 * @param name exception name
 * @param exceptionType exception variance type
 * @param exceptionCode exception variance code
 * @param exceptionDescription exception variance description
 * @param exceptionHandler exception variance handler reference
 * @param exceptionStatus exception variance status
 */
public record StepExceptionDefinition(
    int id,
    String name,
    String exceptionType,
    String exceptionCode,
    String exceptionDescription,
    StepEntity exceptionHandler,
    String exceptionStatus) implements StepEntity {
}