package com.minicad.step.model;

import java.util.List;

/**
 * Resolved EXCEPTION_INSTANCE.
 * An exception instance entity.
 *
 * @param id STEP instance id
 * @param name exception instance name
 * @param exceptionDefinition exception variance definition reference
 * @param exceptionContext exception variance context
 * @param exceptionTime exception variance occurrence time
 * @param exceptionHandled exception variance handled flag
 * @param exceptionStatus exception variance status
 */
public record StepExceptionInstance(
    int id,
    String name,
    StepEntity exceptionDefinition,
    String exceptionContext,
    StepEntity exceptionTime,
    boolean exceptionHandled,
    String exceptionStatus) implements StepEntity {
}