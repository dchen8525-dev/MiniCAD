package com.minicad.step.model;

import java.util.List;

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
public record StepJobDefinition(
    int id,
    String name,
    String jobType,
    int jobPriority,
    String jobSchedule,
    List<StepEntity> jobTasks,
    List<StepEntity> jobDependencies,
    String jobStatus) implements StepEntity {
}