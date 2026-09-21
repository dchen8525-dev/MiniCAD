package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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
public final class StepTaskDefinition extends AbstractStepEntity {
    private final String taskType;
    private final int taskPriority;
    private final String taskDescription;
    private final List<String> taskPreconditions;
    private final List<String> taskPostconditions;
    private final String taskStatus;

    public StepTaskDefinition(int id, String name, String taskType, int taskPriority, String taskDescription, List<String> taskPreconditions, List<String> taskPostconditions, String taskStatus) {
        super(id, name);
        this.taskType = taskType;
        this.taskPriority = taskPriority;
        this.taskDescription = taskDescription;
        this.taskPreconditions = taskPreconditions == null ? null : java.util.List.copyOf(taskPreconditions);
        this.taskPostconditions = taskPostconditions == null ? null : java.util.List.copyOf(taskPostconditions);
        this.taskStatus = taskStatus;
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
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("taskType", taskType);
        state.put("taskPriority", taskPriority);
        state.put("taskDescription", taskDescription);
        state.put("taskPreconditions", taskPreconditions);
        state.put("taskPostconditions", taskPostconditions);
        state.put("taskStatus", taskStatus);
        return state;
    }
}
