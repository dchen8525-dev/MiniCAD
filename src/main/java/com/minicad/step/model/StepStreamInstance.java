package com.minicad.step.model;

import java.util.List;

/**
 * Resolved STREAM_INSTANCE.
 * A stream instance entity.
 *
 * @param id STEP instance id
 * @param name stream instance name
 * @param streamDefinition stream variance definition reference
 * @param streamState stream variance state
 * @param streamPosition stream variance position
 * @param streamRate stream variance rate
 * @param streamStatus stream variance status
 */
public record StepStreamInstance(
    int id,
    String name,
    StepEntity streamDefinition,
    String streamState,
    long streamPosition,
    double streamRate,
    String streamStatus) implements StepEntity {
}