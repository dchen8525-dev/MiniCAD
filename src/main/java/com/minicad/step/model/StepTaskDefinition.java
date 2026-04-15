package com.minicad.step.model;

import java.util.List;

/**
 * Resolved TASK_DEFINITION.
 * A task definition entity.
 *
 * @param id STEP instance id
 * @param name task name
 * @param taskType task variance type
 * @param taskPriority task variance priority
 * @param taskDescription task variance description
 * @param taskPreconditions task variance preconditions
 * @param taskPostconditions task variance postconditions
 * @param taskStatus task variance status
 */
public record StepTaskDefinition(
    int id,
    String name,
    String taskType,
    int taskPriority,
    String taskDescription,
    List<String> taskPreconditions,
    List<String> taskPostconditions,
    String taskStatus) implements StepEntity {
}