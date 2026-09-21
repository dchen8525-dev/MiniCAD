package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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
public final class StepTaskRecord extends AbstractStepEntity {
    private final String taskType;
    private final StepEntity taskTarget;
    private final StepEntity taskAssignee;
    private final StepEntity taskStartTime;
    private final StepEntity taskEndTime;
    private final String taskResult;
    private final String taskStatus;

    public StepTaskRecord(int id, String name, String taskType, StepEntity taskTarget, StepEntity taskAssignee, StepEntity taskStartTime, StepEntity taskEndTime, String taskResult, String taskStatus) {
        super(id, name);
        this.taskType = taskType;
        this.taskTarget = taskTarget;
        this.taskAssignee = taskAssignee;
        this.taskStartTime = taskStartTime;
        this.taskEndTime = taskEndTime;
        this.taskResult = taskResult;
        this.taskStatus = taskStatus;
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
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("taskType", taskType);
        state.put("taskTarget", taskTarget);
        state.put("taskAssignee", taskAssignee);
        state.put("taskStartTime", taskStartTime);
        state.put("taskEndTime", taskEndTime);
        state.put("taskResult", taskResult);
        state.put("taskStatus", taskStatus);
        return state;
    }
}
