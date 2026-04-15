package com.minicad.step.model;

import java.util.List;

/**
 * Resolved TIMER_INSTANCE.
 * A timer instance entity.
 *
 * @param id STEP instance id
 * @param name timer instance name
 * @param timerDefinition timer variance definition reference
 * @param timerState timer variance state
 * @param timerStartTime timer variance start time
 * @param timerRemaining timer variance remaining time
 * @param timerStatus timer variance status
 */
public record StepTimerInstance(
    int id,
    String name,
    StepEntity timerDefinition,
    String timerState,
    StepEntity timerStartTime,
    int timerRemaining,
    String timerStatus) implements StepEntity {
}