package com.minicad.step.model;

import com.minicad.step.model.core.base.StepEntity;
import java.util.List;
import java.util.Objects;

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
public final class StepJobInstance implements StepEntity {
    private final int id;
    private final String name;
    private final StepEntity jobDefinition;
    private final String jobState;
    private final StepEntity jobStartTime;
    private final StepEntity jobEndTime;
    private final double jobProgress;
    private final String jobStatus;

    public StepJobInstance(int id, String name, StepEntity jobDefinition, String jobState, StepEntity jobStartTime, StepEntity jobEndTime, double jobProgress, String jobStatus) {
        this.id = id;
        this.name = name;
        this.jobDefinition = jobDefinition;
        this.jobState = jobState;
        this.jobStartTime = jobStartTime;
        this.jobEndTime = jobEndTime;
        this.jobProgress = jobProgress;
        this.jobStatus = jobStatus;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepJobInstance that = (StepJobInstance) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(jobDefinition, that.jobDefinition) && Objects.equals(jobState, that.jobState) && Objects.equals(jobStartTime, that.jobStartTime) && Objects.equals(jobEndTime, that.jobEndTime) && jobProgress == that.jobProgress && Objects.equals(jobStatus, that.jobStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, jobDefinition, jobState, jobStartTime, jobEndTime, jobProgress, jobStatus);
    }

    @Override
    public String toString() {
        return "StepJobInstance{" + "id=" + id + "name=" + name + "jobDefinition=" + jobDefinition + "jobState=" + jobState + "jobStartTime=" + jobStartTime + "jobEndTime=" + jobEndTime + "jobProgress=" + jobProgress + "jobStatus=" + jobStatus + "}";
    }
}