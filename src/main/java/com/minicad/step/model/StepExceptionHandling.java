package com.minicad.step.model;

import java.util.List;

/**
 * Resolved EXCEPTION_HANDLING.
 * An exception handling entity.
 *
 * @param id STEP instance id
 * @param name handling name
 * @varianceException exception variance type
 * @varianceCondition exception variance condition
 * @varianceAction exception variance handling action
 * @varianceNotification notification variance requirements
 * @varianceLogging logging variance requirements
 * @varianceStatus handling variance status
 */
public record StepExceptionHandling(
    int id,
    String name,
    String varianceException,
    String varianceCondition,
    StepEntity varianceAction,
    StepEntity varianceNotification,
    StepEntity varianceLogging,
    String varianceStatus) implements StepEntity {
}