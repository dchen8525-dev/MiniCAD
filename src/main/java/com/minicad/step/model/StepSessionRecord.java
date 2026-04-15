package com.minicad.step.model;

import java.util.List;

/**
 * Resolved SESSION_RECORD.
 * A session record entity.
 *
 * @param id STEP instance id
 * @param name session name
 * @param sessionType session variance type
 * @param sessionHolder session variance holder reference
 * @param sessionStartTime session variance start time
 * @param sessionEndTime session variance end time
 * @param sessionDuration session variance duration
 * @param sessionStatus session variance status
 */
public record StepSessionRecord(
    int id,
    String name,
    String sessionType,
    StepEntity sessionHolder,
    StepEntity sessionStartTime,
    StepEntity sessionEndTime,
    int sessionDuration,
    String sessionStatus) implements StepEntity {
}