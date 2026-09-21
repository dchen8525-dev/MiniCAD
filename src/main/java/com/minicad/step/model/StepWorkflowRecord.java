package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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
public final class StepWorkflowRecord extends AbstractStepEntity {
    private final String workflowType;
    private final StepEntity workflowTarget;
    private final StepEntity workflowStartTime;
    private final StepEntity workflowEndTime;
    private final String workflowResult;
    private final int workflowSteps;
    private final String workflowStatus;

    public StepWorkflowRecord(int id, String name, String workflowType, StepEntity workflowTarget, StepEntity workflowStartTime, StepEntity workflowEndTime, String workflowResult, int workflowSteps, String workflowStatus) {
        super(id, name);
        this.workflowType = workflowType;
        this.workflowTarget = workflowTarget;
        this.workflowStartTime = workflowStartTime;
        this.workflowEndTime = workflowEndTime;
        this.workflowResult = workflowResult;
        this.workflowSteps = workflowSteps;
        this.workflowStatus = workflowStatus;
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
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("workflowType", workflowType);
        state.put("workflowTarget", workflowTarget);
        state.put("workflowStartTime", workflowStartTime);
        state.put("workflowEndTime", workflowEndTime);
        state.put("workflowResult", workflowResult);
        state.put("workflowSteps", workflowSteps);
        state.put("workflowStatus", workflowStatus);
        return state;
    }
}
