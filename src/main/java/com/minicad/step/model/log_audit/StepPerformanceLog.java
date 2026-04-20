package com.minicad.step.model.log_audit;

import com.minicad.step.model.base.StepEntity;
import java.util.List;

/**
 * Resolved PERFORMANCE_LOG.
 * A performance log entity.
 *
 * @param id STEP instance id
 * @param name log name
 * @param logType log variance type
 * @param logEntries log variance entry list
 * @param logStartTime log variance start time
 * @param logEndTime log variance end time
 * @param logStatus log variance status
 */
public record StepPerformanceLog(
    int id,
    String name,
    String logType,
    List<StepEntity> logEntries,
    StepEntity logStartTime,
    StepEntity logEndTime,
    String logStatus) implements StepEntity {
}