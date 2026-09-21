package com.minicad.step.model;

import java.util.LinkedHashMap;
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
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("assemblyOperations", assemblyOperations);
        state.put("sequenceOrder", sequenceOrder);
        state.put("assemblyFixture", assemblyFixture);
        state.put("assemblyTools", assemblyTools);
        state.put("estimatedTime", estimatedTime);
        state.put("sequenceDependencies", sequenceDependencies);
        return state;
    }
}
