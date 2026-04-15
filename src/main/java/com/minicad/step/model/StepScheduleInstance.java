package com.minicad.step.model;

import java.util.List;

/**
 * Resolved SCHEDULE_INSTANCE.
 * A schedule instance entity.
 *
 * @param id STEP instance id
 * @param name schedule instance name
 * @param scheduleDefinition schedule variance definition reference
 * @param scheduleProgress schedule variance progress
 * @param scheduleActuals schedule variance actual values
 * @param scheduleStatus schedule variance status
 */
public record StepScheduleInstance(
    int id,
    String name,
    StepEntity scheduleDefinition,
    double scheduleProgress,
    List<String> scheduleActuals,
    String scheduleStatus) implements StepEntity {
}