package com.minicad.step.model.workflow;

import com.minicad.step.model.base.StepEntity;
import java.util.List;

/**
 * Resolved RETRY_INSTANCE.
 * A retry instance entity.
 *
 * @param id STEP instance id
 * @param name retry instance name
 * @param retryDefinition retry variance definition reference
 * @param retryState retry variance state
 * @param retryAttempt retry variance current attempt
 * @param retryRemaining retry variance remaining attempts
 * @param retryLastError retry variance last error
 * @param retryStatus retry variance status
 */
public record StepRetryInstance(
    int id,
    String name,
    StepEntity retryDefinition,
    String retryState,
    int retryAttempt,
    int retryRemaining,
    String retryLastError,
    String retryStatus) implements StepEntity {
}