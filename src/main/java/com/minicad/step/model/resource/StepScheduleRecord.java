package com.minicad.step.model.resource;

import com.minicad.step.model.base.StepEntity;
import java.util.List;

/**
 * Resolved SCHEDULE_RECORD.
 * A schedule record entity.
 *
 * @param id STEP instance id
 * @param name schedule name
 * @param scheduleType schedule variance type
 * @param scheduleTarget schedule variance target reference
 * @param scheduleTime schedule variance scheduled time
 * @param scheduleExecutionTime schedule variance execution time
 * @param scheduleResult schedule variance result
 * @param scheduleStatus schedule variance status
 */
public record StepScheduleRecord(
    int id,
    String name,
    String scheduleType,
    StepEntity scheduleTarget,
    StepEntity scheduleTime,
    StepEntity scheduleExecutionTime,
    String scheduleResult,
    String scheduleStatus) implements StepEntity {
}