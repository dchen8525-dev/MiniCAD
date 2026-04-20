package com.minicad.step.model.workflow;

import com.minicad.step.model.base.StepEntity;
import java.util.List;

/**
 * Resolved EXECUTION_TRACE.
 * An execution trace entity.
 *
 * @param id STEP instance id
 * @param name trace name
 * @param traceType trace variance type
 * @param traceEntries trace variance trace entries
 * @param traceStartTime trace variance start time
 * @param traceEndTime trace variance end time
 * @param traceStatus trace variance status
 */
public record StepExecutionTrace(
    int id,
    String name,
    String traceType,
    List<String> traceEntries,
    StepEntity traceStartTime,
    StepEntity traceEndTime,
    String traceStatus) implements StepEntity {
}