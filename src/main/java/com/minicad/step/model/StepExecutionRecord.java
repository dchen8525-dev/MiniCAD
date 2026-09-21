package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Resolved EXECUTION_RECORD.
 * An execution record entity.
 *
 * @param id STEP instance id
 * @param name execution name
 * @param executionType execution variance type
 * @param executionTarget execution variance target reference
 * @param executionStartTime execution variance start time
 * @param executionEndTime execution variance end time
 * @param executionResult execution variance result
 * @param executionDetails execution variance details
 * @param executionStatus execution variance status
 */
public final class StepExecutionRecord extends AbstractStepEntity {
    private final String executionType;
    private final StepEntity executionTarget;
    private final StepEntity executionStartTime;
    private final StepEntity executionEndTime;
    private final String executionResult;
    private final List<String> executionDetails;
    private final String executionStatus;

    public StepExecutionRecord(int id, String name, String executionType, StepEntity executionTarget, StepEntity executionStartTime, StepEntity executionEndTime, String executionResult, List<String> executionDetails, String executionStatus) {
        super(id, name);
        this.executionType = executionType;
        this.executionTarget = executionTarget;
        this.executionStartTime = executionStartTime;
        this.executionEndTime = executionEndTime;
        this.executionResult = executionResult;
        this.executionDetails = executionDetails == null ? null : java.util.List.copyOf(executionDetails);
        this.executionStatus = executionStatus;
    }

    public String getExecutionType() {
        return executionType;
    }

    public StepEntity getExecutionTarget() {
        return executionTarget;
    }

    public StepEntity getExecutionStartTime() {
        return executionStartTime;
    }

    public StepEntity getExecutionEndTime() {
        return executionEndTime;
    }

    public String getExecutionResult() {
        return executionResult;
    }

    public List<String> getExecutionDetails() {
        return executionDetails;
    }

    public String getExecutionStatus() {
        return executionStatus;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("executionType", executionType);
        state.put("executionTarget", executionTarget);
        state.put("executionStartTime", executionStartTime);
        state.put("executionEndTime", executionEndTime);
        state.put("executionResult", executionResult);
        state.put("executionDetails", executionDetails);
        state.put("executionStatus", executionStatus);
        return state;
    }
}
