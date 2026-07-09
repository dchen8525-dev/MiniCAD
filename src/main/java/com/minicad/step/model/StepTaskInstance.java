package com.minicad.step.model;

import com.minicad.step.model.StepEntity;
import java.util.Objects;
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
public final class StepTaskInstance implements StepEntity {
    private final int id;
    private final String name;
    private final StepEntity taskDefinition;
    private final String taskState;
    private final StepEntity taskAssignee;
    private final StepEntity taskStartTime;
    private final StepEntity taskEndTime;
    private final String taskStatus;

    public StepTaskInstance(int id, String name, StepEntity taskDefinition, String taskState, StepEntity taskAssignee, StepEntity taskStartTime, StepEntity taskEndTime, String taskStatus) {
        this.id = id;
        this.name = name;
        this.taskDefinition = taskDefinition;
        this.taskState = taskState;
        this.taskAssignee = taskAssignee;
        this.taskStartTime = taskStartTime;
        this.taskEndTime = taskEndTime;
        this.taskStatus = taskStatus;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public StepEntity getTaskDefinition() {
        return taskDefinition;
    }

    public String getTaskState() {
        return taskState;
    }

    public StepEntity getTaskAssignee() {
        return taskAssignee;
    }

    public StepEntity getTaskStartTime() {
        return taskStartTime;
    }

    public StepEntity getTaskEndTime() {
        return taskEndTime;
    }

    public String getTaskStatus() {
        return taskStatus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepTaskInstance that = (StepTaskInstance) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(taskDefinition, that.taskDefinition) && Objects.equals(taskState, that.taskState) && Objects.equals(taskAssignee, that.taskAssignee) && Objects.equals(taskStartTime, that.taskStartTime) && Objects.equals(taskEndTime, that.taskEndTime) && Objects.equals(taskStatus, that.taskStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, taskDefinition, taskState, taskAssignee, taskStartTime, taskEndTime, taskStatus);
    }

    @Override
    public String toString() {
        return "StepTaskInstance{" + "id=" + id + "name=" + name + "taskDefinition=" + taskDefinition + "taskState=" + taskState + "taskAssignee=" + taskAssignee + "taskStartTime=" + taskStartTime + "taskEndTime=" + taskEndTime + "taskStatus=" + taskStatus + "}";
    }
}