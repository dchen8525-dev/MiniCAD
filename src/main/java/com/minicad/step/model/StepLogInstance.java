package com.minicad.step.model;

import java.util.List;

/**
 * Resolved LOG_INSTANCE.
 * A log instance entity.
 *
 * @param id STEP instance id
 * @param name log instance name
 * @param logDefinition log variance definition reference
 * @param logEntries log variance entries
 * @param logSize log variance size
 * @param logStartTime log variance start time
 * @param logEndTime log variance end time
 * @param logStatus log variance status
 */
public record StepLogInstance(
    int id,
    String name,
    StepEntity logDefinition,
    List<String> logEntries,
    long logSize,
    StepEntity logStartTime,
    StepEntity logEndTime,
    String logStatus) implements StepEntity {
}