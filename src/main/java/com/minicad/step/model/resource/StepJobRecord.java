package com.minicad.step.model.resource;

import com.minicad.step.model.base.StepEntity;
import java.util.List;
import java.util.Objects;

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
public final class StepJobRecord implements StepEntity {
    private final int id;
    private final String name;
    private final String jobType;
    private final StepEntity jobTarget;
    private final StepEntity jobStartTime;
    private final StepEntity jobEndTime;
    private final String jobResult;
    private final List<String> jobDetails;
    private final String jobStatus;

    public StepJobRecord(int id, String name, String jobType, StepEntity jobTarget, StepEntity jobStartTime, StepEntity jobEndTime, String jobResult, List<String> jobDetails, String jobStatus) {
        this.id = id;
        this.name = name;
        this.jobType = jobType;
        this.jobTarget = jobTarget;
        this.jobStartTime = jobStartTime;
        this.jobEndTime = jobEndTime;
        this.jobResult = jobResult;
        this.jobDetails = jobDetails == null ? null : java.util.List.copyOf(jobDetails);
        this.jobStatus = jobStatus;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepJobRecord that = (StepJobRecord) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(jobType, that.jobType) && Objects.equals(jobTarget, that.jobTarget) && Objects.equals(jobStartTime, that.jobStartTime) && Objects.equals(jobEndTime, that.jobEndTime) && Objects.equals(jobResult, that.jobResult) && Objects.equals(jobDetails, that.jobDetails) && Objects.equals(jobStatus, that.jobStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, jobType, jobTarget, jobStartTime, jobEndTime, jobResult, jobDetails, jobStatus);
    }

    @Override
    public String toString() {
        return "StepJobRecord{" + "id=" + id + "name=" + name + "jobType=" + jobType + "jobTarget=" + jobTarget + "jobStartTime=" + jobStartTime + "jobEndTime=" + jobEndTime + "jobResult=" + jobResult + "jobDetails=" + jobDetails + "jobStatus=" + jobStatus + "}";
    }
}