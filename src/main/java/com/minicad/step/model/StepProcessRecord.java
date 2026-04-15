package com.minicad.step.model;

import java.util.List;

/**
 * Resolved PROCESS_RECORD.
 * A process record entity.
 *
 * @param id STEP instance id
 * @param name process name
 * @param processType process variance type
 * @param processTarget process variance target reference
 * @param processStartTime process variance start time
 * @param processEndTime process variance end time
 * @param processResult process variance result
 * @param processDetails process variance details
 * @param processStatus process variance status
 */
public record StepProcessRecord(
    int id,
    String name,
    String processType,
    StepEntity processTarget,
    StepEntity processStartTime,
    StepEntity processEndTime,
    String processResult,
    List<String> processDetails,
    String processStatus) implements StepEntity {
}