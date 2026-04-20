package com.minicad.step.model.workflow;

import com.minicad.step.model.base.StepEntity;
import java.util.List;

/**
 * Resolved SCHEDULER_DEFINITION.
 * A scheduler definition entity.
 *
 * @param id STEP instance id
 * @param name scheduler name
 * @param schedulerType scheduler variance type
 * @param schedulerPolicy scheduler variance scheduling policy
 * @param schedulerInterval scheduler variance interval
 * @param schedulerJobs scheduler variance job definitions
 * @param schedulerStatus scheduler variance status
 */
public record StepSchedulerDefinition(
    int id,
    String name,
    String schedulerType,
    String schedulerPolicy,
    int schedulerInterval,
    List<StepEntity> schedulerJobs,
    String schedulerStatus) implements StepEntity {
}