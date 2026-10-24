package com.minicad.step.model;

import java.util.List;
import java.util.Map;

/**
 * Resolved WORKFLOW_INSTANCE.
 * A workflow instance entity.
 *
 * @param id STEP instance id
 * @param name workflow instance name
 * @param workflowDefinition workflow variance definition reference
 * @param workflowState workflow variance current state
 * @param workflowProgress workflow variance progress percentage
 * @param workflowStartTime workflow variance start time
 * @param workflowEndTime workflow variance end time
 * @param workflowStatus workflow variance status
 */
public final class StepWorkflowInstance extends AbstractStepEntity {
    private final StepEntity workflowDefinition;
    private final String workflowState;
    private final double workflowProgress;
    private final StepEntity workflowStartTime;
    private final StepEntity workflowEndTime;
    private final String workflowStatus;

    public StepWorkflowInstance(int id, String name, StepEntity workflowDefinition, String workflowState, double workflowProgress, StepEntity workflowStartTime, StepEntity workflowEndTime, String workflowStatus) {
        super(id, name);
        this.workflowDefinition = workflowDefinition;
        this.workflowState = workflowState;
        this.workflowProgress = workflowProgress;
        this.workflowStartTime = workflowStartTime;
        this.workflowEndTime = workflowEndTime;
        this.workflowStatus = workflowStatus;
    }

    public StepEntity getWorkflowDefinition() {
        return workflowDefinition;
    }

    public String getWorkflowState() {
        return workflowState;
    }

    public double getWorkflowProgress() {
        return workflowProgress;
    }

    public StepEntity getWorkflowStartTime() {
        return workflowStartTime;
    }

    public StepEntity getWorkflowEndTime() {
        return workflowEndTime;
    }

    public String getWorkflowStatus() {
        return workflowStatus;
    }

    @Override
    protected Map<String, Object> components() {
        return componentsOf(
                "id", getId(), "name", getName(), "workflowDefinition", workflowDefinition,
                "workflowState", workflowState, "workflowProgress", workflowProgress,
                "workflowStartTime", workflowStartTime, "workflowEndTime", workflowEndTime,
                "workflowStatus", workflowStatus);
    }
}
