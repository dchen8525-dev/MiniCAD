package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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
public final class StepJobDefinition extends AbstractStepEntity {
    private final String jobType;
    private final int jobPriority;
    private final String jobSchedule;
    private final List<StepEntity> jobTasks;
    private final List<StepEntity> jobDependencies;
    private final String jobStatus;

    public StepJobDefinition(int id, String name, String jobType, int jobPriority, String jobSchedule, List<StepEntity> jobTasks, List<StepEntity> jobDependencies, String jobStatus) {
        super(id, name);
        this.jobType = jobType;
        this.jobPriority = jobPriority;
        this.jobSchedule = jobSchedule;
        this.jobTasks = jobTasks == null ? null : java.util.List.copyOf(jobTasks);
        this.jobDependencies = jobDependencies == null ? null : java.util.List.copyOf(jobDependencies);
        this.jobStatus = jobStatus;
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
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("jobType", jobType);
        state.put("jobPriority", jobPriority);
        state.put("jobSchedule", jobSchedule);
        state.put("jobTasks", jobTasks);
        state.put("jobDependencies", jobDependencies);
        state.put("jobStatus", jobStatus);
        return state;
    }
}
