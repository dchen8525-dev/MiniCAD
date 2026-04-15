package com.minicad.step.model;

import java.util.List;

/**
 * Resolved TIMEOUT_DEFINITION.
 * A timeout definition entity.
 *
 * @param id STEP instance id
 * @param name timeout name
 * @param timeoutType timeout variance type
 * @param timeoutDuration timeout variance duration in seconds
 * @param timeoutAction timeout variance action on timeout
 * @param timeoutGracePeriod timeout variance grace period
 * @param timeoutStatus timeout variance status
 */
public record StepTimeoutDefinition2(
    int id,
    String name,
    String timeoutType,
    int timeoutDuration,
    StepEntity timeoutAction,
    int timeoutGracePeriod,
    String timeoutStatus) implements StepEntity {
}