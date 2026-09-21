package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.Map;

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
public final class StepTaskInstance extends AbstractStepEntity {
    private final StepEntity taskDefinition;
    private final String taskState;
    private final StepEntity taskAssignee;
    private final StepEntity taskStartTime;
    private final StepEntity taskEndTime;
    private final String taskStatus;

    public StepTaskInstance(int id, String name, StepEntity taskDefinition, String taskState, StepEntity taskAssignee, StepEntity taskStartTime, StepEntity taskEndTime, String taskStatus) {
        super(id, name);
        this.taskDefinition = taskDefinition;
        this.taskState = taskState;
        this.taskAssignee = taskAssignee;
        this.taskStartTime = taskStartTime;
        this.taskEndTime = taskEndTime;
        this.taskStatus = taskStatus;
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
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("taskDefinition", taskDefinition);
        state.put("taskState", taskState);
        state.put("taskAssignee", taskAssignee);
        state.put("taskStartTime", taskStartTime);
        state.put("taskEndTime", taskEndTime);
        state.put("taskStatus", taskStatus);
        return state;
    }
}
