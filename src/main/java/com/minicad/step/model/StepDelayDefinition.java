package com.minicad.step.model;

import java.util.List;

/**
 * Resolved DELAY_DEFINITION.
 * A delay definition entity.
 *
 * @param id STEP instance id
 * @param name delay name
 * @param delayType delay variance type
 * @param delayDuration delay variance duration
 * @param delayCondition delay variance condition
 * @param delayAction delay variance action after delay
 * @param delayStatus delay variance status
 */
public record StepDelayDefinition(
    int id,
    String name,
    String delayType,
    int delayDuration,
    String delayCondition,
    StepEntity delayAction,
    String delayStatus) implements StepEntity {
}