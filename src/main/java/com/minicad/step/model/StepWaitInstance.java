package com.minicad.step.model;

import java.util.List;

/**
 * Resolved WAIT_INSTANCE.
 * A wait instance entity.
 *
 * @param id STEP instance id
 * @param name wait instance name
 * @param waitDefinition wait variance definition reference
 * @param waitState wait variance state
 * @param waitStartTime wait variance start time
 * @param waitConditionMet wait variance condition met flag
 * @param waitStatus wait variance status
 */
public record StepWaitInstance(
    int id,
    String name,
    StepEntity waitDefinition,
    String waitState,
    StepEntity waitStartTime,
    boolean waitConditionMet,
    String waitStatus) implements StepEntity {
}