package com.minicad.step.model;

import com.minicad.step.model.StepEntity;
import java.util.List;
import java.util.Objects;

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
public final class StepTaskRecord implements StepEntity {
    private final int id;
    private final String name;
    private final String taskType;
    private final StepEntity taskTarget;
    private final StepEntity taskAssignee;
    private final StepEntity taskStartTime;
    private final StepEntity taskEndTime;
    private final String taskResult;
    private final String taskStatus;

    public StepTaskRecord(int id, String name, String taskType, StepEntity taskTarget, StepEntity taskAssignee, StepEntity taskStartTime, StepEntity taskEndTime, String taskResult, String taskStatus) {
        this.id = id;
        this.name = name;
        this.taskType = taskType;
        this.taskTarget = taskTarget;
        this.taskAssignee = taskAssignee;
        this.taskStartTime = taskStartTime;
        this.taskEndTime = taskEndTime;
        this.taskResult = taskResult;
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

    public StepEntity getTaskTarget() {
        return taskTarget;
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

    public String getTaskResult() {
        return taskResult;
    }

    public String getTaskStatus() {
        return taskStatus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepTaskRecord that = (StepTaskRecord) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(taskType, that.taskType) && Objects.equals(taskTarget, that.taskTarget) && Objects.equals(taskAssignee, that.taskAssignee) && Objects.equals(taskStartTime, that.taskStartTime) && Objects.equals(taskEndTime, that.taskEndTime) && Objects.equals(taskResult, that.taskResult) && Objects.equals(taskStatus, that.taskStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, taskType, taskTarget, taskAssignee, taskStartTime, taskEndTime, taskResult, taskStatus);
    }

    @Override
    public String toString() {
        return "StepTaskRecord{" + "id=" + id + "name=" + name + "taskType=" + taskType + "taskTarget=" + taskTarget + "taskAssignee=" + taskAssignee + "taskStartTime=" + taskStartTime + "taskEndTime=" + taskEndTime + "taskResult=" + taskResult + "taskStatus=" + taskStatus + "}";
    }
}