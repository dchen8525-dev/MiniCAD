package com.minicad.step.model.workflow;

import com.minicad.step.model.base.StepEntity;
import java.util.List;

/**
 * Resolved TRAIL_INSTANCE.
 * A trail instance entity.
 *
 * @param id STEP instance id
 * @param name trail instance name
 * @param trailDefinition trail variance definition reference
 * @param trailEntries trail variance entry count
 * @param trailStartTime trail variance start time
 * @param trailEndTime trail variance end time
 * @param trailStatus trail variance status
 */
public record StepTrailInstance(
    int id,
    String name,
    StepEntity trailDefinition,
    int trailEntries,
    StepEntity trailStartTime,
    StepEntity trailEndTime,
    String trailStatus) implements StepEntity {
}