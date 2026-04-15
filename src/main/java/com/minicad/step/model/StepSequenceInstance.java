package com.minicad.step.model;

import java.util.List;

/**
 * Resolved SEQUENCE_INSTANCE.
 * A sequence instance entity.
 *
 * @param id STEP instance id
 * @param name sequence instance name
 * @param sequenceDefinition sequence variance definition reference
 * @param sequenceState sequence variance state
 * @param sequencePosition sequence variance current position
 * @param sequenceCompleted sequence variance completed items
 * @param sequenceStatus sequence variance status
 */
public record StepSequenceInstance(
    int id,
    String name,
    StepEntity sequenceDefinition,
    String sequenceState,
    int sequencePosition,
    int sequenceCompleted,
    String sequenceStatus) implements StepEntity {
}