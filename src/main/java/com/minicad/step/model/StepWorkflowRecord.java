package com.minicad.step.model;

import com.minicad.step.model.StepEntity;
import java.util.List;
import java.util.Objects;

/**
 * Resolved WORKFLOW_RECORD.
 * A workflow record entity.
 *
 * @param id STEP instance id
 * @param name workflow name
 * @param workflowType workflow variance type
 * @param workflowTarget workflow variance target reference
 * @param workflowStartTime workflow variance start time
 * @param workflowEndTime workflow variance end time
 * @param workflowResult workflow variance result
 * @param workflowSteps workflow variance completed steps
 * @param workflowStatus workflow variance status
 */
/**
 * Resolved WORKFLOW_RECORD.
 * A workflow record entity.
 *
 * @param id STEP instance id
 * @param name workflow name
 * @param workflowType workflow variance type
 * @param workflowTarget workflow variance target reference
 * @param workflowStartTime workflow variance start time
 * @param workflowEndTime workflow variance end time
 * @param workflowResult workflow variance result
 * @param workflowSteps workflow variance completed steps
 * @param workflowStatus workflow variance status
 */
public final class StepWorkflowRecord implements StepEntity {
    private final int id;
    private final String name;
    private final String workflowType;
    private final StepEntity workflowTarget;
    private final StepEntity workflowStartTime;
    private final StepEntity workflowEndTime;
    private final String workflowResult;
    private final int workflowSteps;
    private final String workflowStatus;

    public StepWorkflowRecord(int id, String name, String workflowType, StepEntity workflowTarget, StepEntity workflowStartTime, StepEntity workflowEndTime, String workflowResult, int workflowSteps, String workflowStatus) {
        this.id = id;
        this.name = name;
        this.workflowType = workflowType;
        this.workflowTarget = workflowTarget;
        this.workflowStartTime = workflowStartTime;
        this.workflowEndTime = workflowEndTime;
        this.workflowResult = workflowResult;
        this.workflowSteps = workflowSteps;
        this.workflowStatus = workflowStatus;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getWorkflowType() {
        return workflowType;
    }

    public StepEntity getWorkflowTarget() {
        return workflowTarget;
    }

    public StepEntity getWorkflowStartTime() {
        return workflowStartTime;
    }

    public StepEntity getWorkflowEndTime() {
        return workflowEndTime;
    }

    public String getWorkflowResult() {
        return workflowResult;
    }

    public int getWorkflowSteps() {
        return workflowSteps;
    }

    public String getWorkflowStatus() {
        return workflowStatus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepWorkflowRecord that = (StepWorkflowRecord) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(workflowType, that.workflowType) && Objects.equals(workflowTarget, that.workflowTarget) && Objects.equals(workflowStartTime, that.workflowStartTime) && Objects.equals(workflowEndTime, that.workflowEndTime) && Objects.equals(workflowResult, that.workflowResult) && workflowSteps == that.workflowSteps && Objects.equals(workflowStatus, that.workflowStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, workflowType, workflowTarget, workflowStartTime, workflowEndTime, workflowResult, workflowSteps, workflowStatus);
    }

    @Override
    public String toString() {
        return "StepWorkflowRecord{" + "id=" + id + "name=" + name + "workflowType=" + workflowType + "workflowTarget=" + workflowTarget + "workflowStartTime=" + workflowStartTime + "workflowEndTime=" + workflowEndTime + "workflowResult=" + workflowResult + "workflowSteps=" + workflowSteps + "workflowStatus=" + workflowStatus + "}";
    }
}