package com.minicad.step.model;

import java.util.List;

/**
 * Resolved PAUSE_DEFINITION.
 * A pause definition entity.
 *
 * @param id STEP instance id
 * @param name pause name
 * @param pauseType pause variance type
 * @param pauseCondition pause variance condition
 * @param pauseResumeCondition pause variance resume condition
 * @param pauseTimeout pause variance max pause time
 * @param pauseStatus pause variance status
 */
public record StepPauseDefinition(
    int id,
    String name,
    String pauseType,
    String pauseCondition,
    String pauseResumeCondition,
    int pauseTimeout,
    String pauseStatus) implements StepEntity {
}