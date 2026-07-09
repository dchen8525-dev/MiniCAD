package com.minicad.step.model;

import com.minicad.step.model.core.base.StepEntity;
import java.util.List;
import java.util.Objects;

/**
 * Resolved JOB_DEFINITION.
 * A job definition entity.
 *
 * @param id STEP instance id
 * @param name job name
 * @param jobType job variance type
 * @param jobPriority job variance priority
 * @param jobSchedule job variance schedule
 * @param jobTasks job variance task definitions
 * @param jobDependencies job variance dependencies
 * @param jobStatus job variance status
 */
/**
 * Resolved JOB_DEFINITION.
 * A job definition entity.
 *
 * @param id STEP instance id
 * @param name job name
 * @param jobType job variance type
 * @param jobPriority job variance priority
 * @param jobSchedule job variance schedule
 * @param jobTasks job variance task definitions
 * @param jobDependencies job variance dependencies
 * @param jobStatus job variance status
 */
public final class StepJobDefinition implements StepEntity {
    private final int id;
    private final String name;
    private final String jobType;
    private final int jobPriority;
    private final String jobSchedule;
    private final List<StepEntity> jobTasks;
    private final List<StepEntity> jobDependencies;
    private final String jobStatus;

    public StepJobDefinition(int id, String name, String jobType, int jobPriority, String jobSchedule, List<StepEntity> jobTasks, List<StepEntity> jobDependencies, String jobStatus) {
        this.id = id;
        this.name = name;
        this.jobType = jobType;
        this.jobPriority = jobPriority;
        this.jobSchedule = jobSchedule;
        this.jobTasks = jobTasks == null ? null : java.util.List.copyOf(jobTasks);
        this.jobDependencies = jobDependencies == null ? null : java.util.List.copyOf(jobDependencies);
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

    public int getJobPriority() {
        return jobPriority;
    }

    public String getJobSchedule() {
        return jobSchedule;
    }

    public List<StepEntity> getJobTasks() {
        return jobTasks;
    }

    public List<StepEntity> getJobDependencies() {
        return jobDependencies;
    }

    public String getJobStatus() {
        return jobStatus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepJobDefinition that = (StepJobDefinition) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(jobType, that.jobType) && jobPriority == that.jobPriority && Objects.equals(jobSchedule, that.jobSchedule) && Objects.equals(jobTasks, that.jobTasks) && Objects.equals(jobDependencies, that.jobDependencies) && Objects.equals(jobStatus, that.jobStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, jobType, jobPriority, jobSchedule, jobTasks, jobDependencies, jobStatus);
    }

    @Override
    public String toString() {
        return "StepJobDefinition{" + "id=" + id + "name=" + name + "jobType=" + jobType + "jobPriority=" + jobPriority + "jobSchedule=" + jobSchedule + "jobTasks=" + jobTasks + "jobDependencies=" + jobDependencies + "jobStatus=" + jobStatus + "}";
    }
}