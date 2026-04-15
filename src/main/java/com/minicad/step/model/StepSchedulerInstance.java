package com.minicad.step.model;

import java.util.List;

/**
 * Resolved SCHEDULER_INSTANCE.
 * A scheduler instance entity.
 *
 * @param id STEP instance id
 * @param name scheduler instance name
 * @param schedulerDefinition scheduler variance definition reference
 * @param schedulerState scheduler variance state
 * @param schedulerActiveJobs scheduler variance active job count
 * @param schedulerPendingJobs scheduler variance pending job count
 * @param schedulerCompletedJobs scheduler variance completed job count
 * @param schedulerStatus scheduler variance status
 */
public record StepSchedulerInstance(
    int id,
    String name,
    StepEntity schedulerDefinition,
    String schedulerState,
    int schedulerActiveJobs,
    int schedulerPendingJobs,
    int schedulerCompletedJobs,
    String schedulerStatus) implements StepEntity {
}