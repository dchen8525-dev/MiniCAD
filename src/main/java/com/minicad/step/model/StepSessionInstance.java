package com.minicad.step.model;

import java.util.List;

/**
 * Resolved SESSION_INSTANCE.
 * A session instance entity.
 *
 * @param id STEP instance id
 * @param name session instance name
 * @param sessionDefinition session variance definition reference
 * @param sessionState session variance state
 * @param sessionUser session variance user reference
 * @param sessionStartTime session variance start time
 * @param sessionLastActivity session variance last activity time
 * @param sessionStatus session variance status
 */
public record StepSessionInstance(
    int id,
    String name,
    StepEntity sessionDefinition,
    String sessionState,
    StepEntity sessionUser,
    StepEntity sessionStartTime,
    StepEntity sessionLastActivity,
    String sessionStatus) implements StepEntity {
}