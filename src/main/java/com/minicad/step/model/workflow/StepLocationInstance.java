package com.minicad.step.model.workflow;

import com.minicad.step.model.base.StepEntity;
/**
 * Resolved LOCATION_INSTANCE.
 * A location instance entity.
 *
 * @param id STEP instance id
 * @param name location instance name
 * @param locationDefinition location variance definition reference
 * @param locationState location variance state
 * @param locationCapacity location variance capacity
 * @param locationStatus location variance status
 */
public record StepLocationInstance(
    int id,
    String name,
    StepEntity locationDefinition,
    String locationState,
    double locationCapacity,
    String locationStatus) implements StepEntity {
}