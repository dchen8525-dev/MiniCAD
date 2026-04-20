package com.minicad.step.model.workflow;

import com.minicad.step.model.base.StepEntity;
import java.util.List;

/**
 * Resolved PAUSE_INSTANCE.
 * A pause instance entity.
 *
 * @param id STEP instance id
 * @param name pause instance name
 * @param pauseDefinition pause variance definition reference
 * @param pauseState pause variance state
 * @param pauseStartTime pause variance start time
 * @param pauseDuration pause variance current duration
 * @param pauseStatus pause variance status
 */
public record StepPauseInstance(
    int id,
    String name,
    StepEntity pauseDefinition,
    String pauseState,
    StepEntity pauseStartTime,
    int pauseDuration,
    String pauseStatus) implements StepEntity {
}