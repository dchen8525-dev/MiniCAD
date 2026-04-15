package com.minicad.step.model;

import java.util.List;

/**
 * Resolved BUFFER_DEFINITION.
 * A buffer definition entity.
 *
 * @param id STEP instance id
 * @param name buffer name
 * @param bufferType buffer variance type
 * @param bufferCapacity buffer variance capacity
 * @param bufferPolicy buffer variance policy
 * @param bufferStatus buffer variance status
 */
public record StepBufferDefinition(
    int id,
    String name,
    String bufferType,
    int bufferCapacity,
    String bufferPolicy,
    String bufferStatus) implements StepEntity {
}