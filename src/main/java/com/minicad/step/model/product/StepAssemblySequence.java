package com.minicad.step.model.product;

import com.minicad.step.model.core.base.StepEntity;
import java.util.List;
import java.util.Objects;

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
public final class StepAssemblySequence implements StepEntity {
    private final int id;
    private final String name;
    private final List<StepEntity> assemblyOperations;
    private final List<Integer> sequenceOrder;
    private final StepEntity assemblyFixture;
    private final List<StepEntity> assemblyTools;
    private final double estimatedTime;
    private final List<StepEntity> sequenceDependencies;

    public StepAssemblySequence(int id, String name, List<StepEntity> assemblyOperations, List<Integer> sequenceOrder, StepEntity assemblyFixture, List<StepEntity> assemblyTools, double estimatedTime, List<StepEntity> sequenceDependencies) {
        this.id = id;
        this.name = name;
        this.assemblyOperations = assemblyOperations == null ? null : java.util.List.copyOf(assemblyOperations);
        this.sequenceOrder = sequenceOrder == null ? null : java.util.List.copyOf(sequenceOrder);
        this.assemblyFixture = assemblyFixture;
        this.assemblyTools = assemblyTools == null ? null : java.util.List.copyOf(assemblyTools);
        this.estimatedTime = estimatedTime;
        this.sequenceDependencies = sequenceDependencies == null ? null : java.util.List.copyOf(sequenceDependencies);
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepAssemblySequence that = (StepAssemblySequence) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(assemblyOperations, that.assemblyOperations) && Objects.equals(sequenceOrder, that.sequenceOrder) && Objects.equals(assemblyFixture, that.assemblyFixture) && Objects.equals(assemblyTools, that.assemblyTools) && estimatedTime == that.estimatedTime && Objects.equals(sequenceDependencies, that.sequenceDependencies);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, assemblyOperations, sequenceOrder, assemblyFixture, assemblyTools, estimatedTime, sequenceDependencies);
    }

    @Override
    public String toString() {
        return "StepAssemblySequence{" + "id=" + id + "name=" + name + "assemblyOperations=" + assemblyOperations + "sequenceOrder=" + sequenceOrder + "assemblyFixture=" + assemblyFixture + "assemblyTools=" + assemblyTools + "estimatedTime=" + estimatedTime + "sequenceDependencies=" + sequenceDependencies + "}";
    }
}