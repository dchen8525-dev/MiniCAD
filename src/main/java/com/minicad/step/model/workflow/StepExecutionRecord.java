package com.minicad.step.model.workflow;

import com.minicad.step.model.base.StepEntity;
import java.util.List;

/**
 * Resolved EXECUTION_RECORD.
 * An execution record entity.
 *
 * @param id STEP instance id
 * @param name execution name
 * @param executionType execution variance type
 * @param executionTarget execution variance target reference
 * @param executionStartTime execution variance start time
 * @param executionEndTime execution variance end time
 * @param executionResult execution variance result
 * @param executionDetails execution variance details
 * @param executionStatus execution variance status
 */
public record StepExecutionRecord(
    int id,
    String name,
    String executionType,
    StepEntity executionTarget,
    StepEntity executionStartTime,
    StepEntity executionEndTime,
    String executionResult,
    List<String> executionDetails,
    String executionStatus) implements StepEntity {
}