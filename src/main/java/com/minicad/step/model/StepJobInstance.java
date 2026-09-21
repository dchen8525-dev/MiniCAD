package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Resolved JOB_INSTANCE.
 * A job instance entity.
 *
 * @param id STEP instance id
 * @param name job instance name
 * @param jobDefinition job variance definition reference
 * @param jobState job variance state
 * @param jobStartTime job variance start time
 * @param jobEndTime job variance end time
 * @param jobProgress job variance progress percentage
 * @param jobStatus job variance status
 */
public final class StepJobInstance extends AbstractStepEntity {
    private final StepEntity jobDefinition;
    private final String jobState;
    private final StepEntity jobStartTime;
    private final StepEntity jobEndTime;
    private final double jobProgress;
    private final String jobStatus;

    public StepJobInstance(int id, String name, StepEntity jobDefinition, String jobState, StepEntity jobStartTime, StepEntity jobEndTime, double jobProgress, String jobStatus) {
        super(id, name);
        this.jobDefinition = jobDefinition;
        this.jobState = jobState;
        this.jobStartTime = jobStartTime;
        this.jobEndTime = jobEndTime;
        this.jobProgress = jobProgress;
        this.jobStatus = jobStatus;
    }

    public StepEntity getJobDefinition() {
        return jobDefinition;
    }

    public String getJobState() {
        return jobState;
    }

    public StepEntity getJobStartTime() {
        return jobStartTime;
    }

    public StepEntity getJobEndTime() {
        return jobEndTime;
    }

    public double getJobProgress() {
        return jobProgress;
    }

    public String getJobStatus() {
        return jobStatus;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("jobDefinition", jobDefinition);
        state.put("jobState", jobState);
        state.put("jobStartTime", jobStartTime);
        state.put("jobEndTime", jobEndTime);
        state.put("jobProgress", jobProgress);
        state.put("jobStatus", jobStatus);
        return state;
    }
}
