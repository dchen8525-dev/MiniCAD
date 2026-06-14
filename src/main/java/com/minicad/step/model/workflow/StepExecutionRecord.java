package com.minicad.step.model.workflow;

import com.minicad.step.model.base.StepEntity;
import java.util.List;
import java.util.Objects;

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
public final class StepExecutionRecord implements StepEntity {
    private final int id;
    private final String name;
    private final String executionType;
    private final StepEntity executionTarget;
    private final StepEntity executionStartTime;
    private final StepEntity executionEndTime;
    private final String executionResult;
    private final List<String> executionDetails;
    private final String executionStatus;

    public StepExecutionRecord(int id, String name, String executionType, StepEntity executionTarget, StepEntity executionStartTime, StepEntity executionEndTime, String executionResult, List<String> executionDetails, String executionStatus) {
        this.id = id;
        this.name = name;
        this.executionType = executionType;
        this.executionTarget = executionTarget;
        this.executionStartTime = executionStartTime;
        this.executionEndTime = executionEndTime;
        this.executionResult = executionResult;
        this.executionDetails = executionDetails == null ? null : java.util.List.copyOf(executionDetails);
        this.executionStatus = executionStatus;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepExecutionRecord that = (StepExecutionRecord) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(executionType, that.executionType) && Objects.equals(executionTarget, that.executionTarget) && Objects.equals(executionStartTime, that.executionStartTime) && Objects.equals(executionEndTime, that.executionEndTime) && Objects.equals(executionResult, that.executionResult) && Objects.equals(executionDetails, that.executionDetails) && Objects.equals(executionStatus, that.executionStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, executionType, executionTarget, executionStartTime, executionEndTime, executionResult, executionDetails, executionStatus);
    }

    @Override
    public String toString() {
        return "StepExecutionRecord{" + "id=" + id + "name=" + name + "executionType=" + executionType + "executionTarget=" + executionTarget + "executionStartTime=" + executionStartTime + "executionEndTime=" + executionEndTime + "executionResult=" + executionResult + "executionDetails=" + executionDetails + "executionStatus=" + executionStatus + "}";
    }
}