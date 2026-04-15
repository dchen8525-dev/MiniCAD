package com.minicad.step.model;

/**
 * Resolved BUFFER_INSTANCE.
 * A buffer instance entity.
 *
 * @param id STEP instance id
 * @param name buffer instance name
 * @param bufferDefinition buffer variance definition reference
 * @param bufferState buffer variance state
 * @param bufferUsed buffer variance used bytes
 * @param bufferAvailable buffer variance available bytes
 * @param bufferStatus buffer variance status
 */
public record StepBufferInstance(
    int id,
    String name,
    StepEntity bufferDefinition,
    String bufferState,
    long bufferUsed,
    long bufferAvailable,
    String bufferStatus) implements StepEntity {
}