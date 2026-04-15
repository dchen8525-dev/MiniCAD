package com.minicad.step.model;

import java.util.List;

/**
 * Resolved ACTIVITY_RECORD.
 * An activity record entity.
 *
 * @param id STEP instance id
 * @param name activity name
 * @param activityType activity variance type
 * @param activityAction activity variance action description
 * @param activityActor activity variance actor reference
 * @param activityTarget activity variance target reference
 * @param activityTimestamp activity variance timestamp
 * @param activityDetails activity variance details
 * @param activityStatus activity variance status
 */
public record StepActivityRecord(
    int id,
    String name,
    String activityType,
    String activityAction,
    StepEntity activityActor,
    StepEntity activityTarget,
    StepEntity activityTimestamp,
    List<String> activityDetails,
    String activityStatus) implements StepEntity {
}