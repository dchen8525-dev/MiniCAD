package com.minicad.step.model;

import java.util.List;

/**
 * Resolved DELAY_INSTANCE.
 * A delay instance entity.
 *
 * @param id STEP instance id
 * @param name delay instance name
 * @param delayDefinition delay variance definition reference
 * @param delayState delay variance state
 * @param delayStartTime delay variance start time
 * @param delayEndTime delay variance expected end time
 * @param delayRemaining delay variance remaining time
 * @param delayStatus delay variance status
 */
public record StepDelayInstance(
    int id,
    String name,
    StepEntity delayDefinition,
    String delayState,
    StepEntity delayStartTime,
    StepEntity delayEndTime,
    int delayRemaining,
    String delayStatus) implements StepEntity {
}