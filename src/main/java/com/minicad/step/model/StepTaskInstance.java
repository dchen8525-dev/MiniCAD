package com.minicad.step.model;

/**
 * Resolved TASK_INSTANCE.
 * A task instance entity.
 *
 * @param id STEP instance id
 * @param name task instance name
 * @param taskDefinition task variance definition reference
 * @param taskState task variance current state
 * @param taskAssignee task variance assignee reference
 * @param taskStartTime task variance start time
 * @param taskEndTime task variance end time
 * @param taskStatus task variance status
 */
public record StepTaskInstance(
    int id,
    String name,
    StepEntity taskDefinition,
    String taskState,
    StepEntity taskAssignee,
    StepEntity taskStartTime,
    StepEntity taskEndTime,
    String taskStatus) implements StepEntity {
}