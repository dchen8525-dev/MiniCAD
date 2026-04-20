package com.minicad.step.model.resource;

import com.minicad.step.model.base.StepEntity;
import java.util.List;

/**
 * Resolved TASK_RECORD.
 * A task record entity.
 *
 * @param id STEP instance id
 * @param name task name
 * @param taskType task variance type
 * @param taskTarget task variance target reference
 * @param taskAssignee task variance assignee reference
 * @param taskStartTime task variance start time
 * @param taskEndTime task variance end time
 * @param taskResult task variance result
 * @param taskStatus task variance status
 */
public record StepTaskRecord(
    int id,
    String name,
    String taskType,
    StepEntity taskTarget,
    StepEntity taskAssignee,
    StepEntity taskStartTime,
    StepEntity taskEndTime,
    String taskResult,
    String taskStatus) implements StepEntity {
}