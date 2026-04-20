package com.minicad.step.model.resource;

import com.minicad.step.model.base.StepEntity;
import java.util.List;

/**
 * Resolved SCHEDULE_DEFINITION.
 * A schedule definition entity.
 *
 * @param id STEP instance id
 * @param name schedule name
 * @param scheduleType schedule variance type
 * @param scheduleMilestones schedule variance milestones
 * @param scheduleConstraints schedule variance constraints
 * @param scheduleResources schedule variance resources
 * @param scheduleStatus schedule variance status
 */
public record StepScheduleDefinition(
    int id,
    String name,
    String scheduleType,
    List<StepEntity> scheduleMilestones,
    List<String> scheduleConstraints,
    List<StepEntity> scheduleResources,
    String scheduleStatus) implements StepEntity {
}