package com.minicad.step.model.workflow;

import com.minicad.step.model.base.StepEntity;
import java.util.List;
import java.util.Objects;

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
public final class StepWorkflowInstance implements StepEntity {
    private final int id;
    private final String name;
    private final StepEntity workflowDefinition;
    private final String workflowState;
    private final double workflowProgress;
    private final StepEntity workflowStartTime;
    private final StepEntity workflowEndTime;
    private final String workflowStatus;

    public StepWorkflowInstance(int id, String name, StepEntity workflowDefinition, String workflowState, double workflowProgress, StepEntity workflowStartTime, StepEntity workflowEndTime, String workflowStatus) {
        this.id = id;
        this.name = name;
        this.workflowDefinition = workflowDefinition;
        this.workflowState = workflowState;
        this.workflowProgress = workflowProgress;
        this.workflowStartTime = workflowStartTime;
        this.workflowEndTime = workflowEndTime;
        this.workflowStatus = workflowStatus;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepWorkflowInstance that = (StepWorkflowInstance) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(workflowDefinition, that.workflowDefinition) && Objects.equals(workflowState, that.workflowState) && workflowProgress == that.workflowProgress && Objects.equals(workflowStartTime, that.workflowStartTime) && Objects.equals(workflowEndTime, that.workflowEndTime) && Objects.equals(workflowStatus, that.workflowStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, workflowDefinition, workflowState, workflowProgress, workflowStartTime, workflowEndTime, workflowStatus);
    }

    @Override
    public String toString() {
        return "StepWorkflowInstance{" + "id=" + id + "name=" + name + "workflowDefinition=" + workflowDefinition + "workflowState=" + workflowState + "workflowProgress=" + workflowProgress + "workflowStartTime=" + workflowStartTime + "workflowEndTime=" + workflowEndTime + "workflowStatus=" + workflowStatus + "}";
    }
}