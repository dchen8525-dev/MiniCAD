package com.minicad.step.model;

import java.util.List;

/**
 * Resolved TIMER_DEFINITION.
 * A timer definition entity.
 *
 * @param id STEP instance id
 * @param name timer name
 * @param timerType timer variance type
 * @param timerDuration timer variance duration
 * @param timerAction timer variance action reference
 * @param timerRecurring timer variance recurring flag
 * @param timerStatus timer variance status
 */
public record StepTimerDefinition(
    int id,
    String name,
    String timerType,
    int timerDuration,
    StepEntity timerAction,
    boolean timerRecurring,
    String timerStatus) implements StepEntity {
}