package com.minicad.step.model;

import java.util.List;

/**
 * Resolved PLATFORM_INSTANCE.
 * A platform instance entity.
 *
 * @param id STEP instance id
 * @param name platform instance name
 * @param platformDefinition platform variance definition reference
 * @param platformState platform variance state
 * @param platformVersion platform variance version
 * @param platformHealth platform variance health status
 * @param platformStatus platform variance status
 */
public record StepPlatformInstance(
    int id,
    String name,
    StepEntity platformDefinition,
    String platformState,
    String platformVersion,
    String platformHealth,
    String platformStatus) implements StepEntity {
}