package com.minicad.step.model;

import java.util.List;
import java.util.Map;

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
public final class StepAssemblySequence extends AbstractStepEntity {
    private final List<StepEntity> assemblyOperations;
    private final List<Integer> sequenceOrder;
    private final StepEntity assemblyFixture;
    private final List<StepEntity> assemblyTools;
    private final double estimatedTime;
    private final List<StepEntity> sequenceDependencies;

    public StepAssemblySequence(int id, String name, List<StepEntity> assemblyOperations, List<Integer> sequenceOrder, StepEntity assemblyFixture, List<StepEntity> assemblyTools, double estimatedTime, List<StepEntity> sequenceDependencies) {
        super(id, name);
        this.assemblyOperations = assemblyOperations == null ? null : java.util.List.copyOf(assemblyOperations);
        this.sequenceOrder = sequenceOrder == null ? null : java.util.List.copyOf(sequenceOrder);
        this.assemblyFixture = assemblyFixture;
        this.assemblyTools = assemblyTools == null ? null : java.util.List.copyOf(assemblyTools);
        this.estimatedTime = estimatedTime;
        this.sequenceDependencies = sequenceDependencies == null ? null : java.util.List.copyOf(sequenceDependencies);
    }

    public List<StepEntity> getAssemblyOperations() {
        return assemblyOperations;
    }

    public List<Integer> getSequenceOrder() {
        return sequenceOrder;
    }

    public StepEntity getAssemblyFixture() {
        return assemblyFixture;
    }

    public List<StepEntity> getAssemblyTools() {
        return assemblyTools;
    }

    public double getEstimatedTime() {
        return estimatedTime;
    }

    public List<StepEntity> getSequenceDependencies() {
        return sequenceDependencies;
    }

    @Override
    protected Map<String, Object> components() {
        return componentsOf(
                "id", getId(), "name", getName(), "assemblyOperations", assemblyOperations,
                "sequenceOrder", sequenceOrder, "assemblyFixture", assemblyFixture,
                "assemblyTools", assemblyTools, "estimatedTime", estimatedTime,
                "sequenceDependencies", sequenceDependencies);
    }
}
