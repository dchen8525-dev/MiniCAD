package com.minicad.step.model.product;

import com.minicad.step.model.base.StepEntity;
import java.util.List;

/**
 * Resolved ASSEMBLY_SEQUENCE.
 * An assembly sequence entity.
 *
 * @param id STEP instance id
 * @param name sequence name
 * @param assemblyOperations assembly operations in sequence
 * @param sequenceOrder sequence order specification
 * @param assemblyFixture assembly fixture reference
 * @param assemblyTools assembly tools used
 * @param estimatedTime estimated assembly time
 * @param sequenceDependencies dependencies between operations
 */
public record StepAssemblySequence(
    int id,
    String name,
    List<StepEntity> assemblyOperations,
    List<Integer> sequenceOrder,
    StepEntity assemblyFixture,
    List<StepEntity> assemblyTools,
    double estimatedTime,
    List<StepEntity> sequenceDependencies) implements StepEntity {
}