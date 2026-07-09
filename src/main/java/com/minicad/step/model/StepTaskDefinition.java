package com.minicad.step.model;

import com.minicad.step.model.StepEntity;
import java.util.List;
import java.util.Objects;

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
public final class StepTaskDefinition implements StepEntity {
    private final int id;
    private final String name;
    private final String taskType;
    private final int taskPriority;
    private final String taskDescription;
    private final List<String> taskPreconditions;
    private final List<String> taskPostconditions;
    private final String taskStatus;

    public StepTaskDefinition(int id, String name, String taskType, int taskPriority, String taskDescription, List<String> taskPreconditions, List<String> taskPostconditions, String taskStatus) {
        this.id = id;
        this.name = name;
        this.taskType = taskType;
        this.taskPriority = taskPriority;
        this.taskDescription = taskDescription;
        this.taskPreconditions = taskPreconditions == null ? null : java.util.List.copyOf(taskPreconditions);
        this.taskPostconditions = taskPostconditions == null ? null : java.util.List.copyOf(taskPostconditions);
        this.taskStatus = taskStatus;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getTaskType() {
        return taskType;
    }

    public int getTaskPriority() {
        return taskPriority;
    }

    public String getTaskDescription() {
        return taskDescription;
    }

    public List<String> getTaskPreconditions() {
        return taskPreconditions;
    }

    public List<String> getTaskPostconditions() {
        return taskPostconditions;
    }

    public String getTaskStatus() {
        return taskStatus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepTaskDefinition that = (StepTaskDefinition) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(taskType, that.taskType) && taskPriority == that.taskPriority && Objects.equals(taskDescription, that.taskDescription) && Objects.equals(taskPreconditions, that.taskPreconditions) && Objects.equals(taskPostconditions, that.taskPostconditions) && Objects.equals(taskStatus, that.taskStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, taskType, taskPriority, taskDescription, taskPreconditions, taskPostconditions, taskStatus);
    }

    @Override
    public String toString() {
        return "StepTaskDefinition{" + "id=" + id + "name=" + name + "taskType=" + taskType + "taskPriority=" + taskPriority + "taskDescription=" + taskDescription + "taskPreconditions=" + taskPreconditions + "taskPostconditions=" + taskPostconditions + "taskStatus=" + taskStatus + "}";
    }
}