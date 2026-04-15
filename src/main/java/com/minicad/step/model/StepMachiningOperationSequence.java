package com.minicad.step.model;

import java.util.List;

/**
 * Resolved MACHINING_OPERATION_SEQUENCE.
 * A machining operation sequence entity.
 *
 * @param id STEP instance id
 * @param name sequence name
 * @param operations list of machining operations
 * @param sequenceType sequence type classification
 */
public record StepMachiningOperationSequence(
    int id,
    String name,
    List<StepEntity> operations,
    String sequenceType) implements StepEntity {
}