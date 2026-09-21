package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Resolved JOB_RECORD.
 * A job record entity.
 *
 * @param id STEP instance id
 * @param name job name
 * @param jobType job variance type
 * @param jobTarget job variance target reference
 * @param jobStartTime job variance start time
 * @param jobEndTime job variance end time
 * @param jobResult job variance result
 * @param jobDetails job variance details
 * @param jobStatus job variance status
 */
public final class StepJobRecord extends AbstractStepEntity {
    private final String jobType;
    private final StepEntity jobTarget;
    private final StepEntity jobStartTime;
    private final StepEntity jobEndTime;
    private final String jobResult;
    private final List<String> jobDetails;
    private final String jobStatus;

    public StepJobRecord(int id, String name, String jobType, StepEntity jobTarget, StepEntity jobStartTime, StepEntity jobEndTime, String jobResult, List<String> jobDetails, String jobStatus) {
        super(id, name);
        this.jobType = jobType;
        this.jobTarget = jobTarget;
        this.jobStartTime = jobStartTime;
        this.jobEndTime = jobEndTime;
        this.jobResult = jobResult;
        this.jobDetails = jobDetails == null ? null : java.util.List.copyOf(jobDetails);
        this.jobStatus = jobStatus;
    }

    public String getJobType() {
        return jobType;
    }

    public StepEntity getJobTarget() {
        return jobTarget;
    }

    public StepEntity getJobStartTime() {
        return jobStartTime;
    }

    public StepEntity getJobEndTime() {
        return jobEndTime;
    }

    public String getJobResult() {
        return jobResult;
    }

    public List<String> getJobDetails() {
        return jobDetails;
    }

    public String getJobStatus() {
        return jobStatus;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("jobType", jobType);
        state.put("jobTarget", jobTarget);
        state.put("jobStartTime", jobStartTime);
        state.put("jobEndTime", jobEndTime);
        state.put("jobResult", jobResult);
        state.put("jobDetails", jobDetails);
        state.put("jobStatus", jobStatus);
        return state;
    }
}
