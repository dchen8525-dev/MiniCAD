package com.minicad.step.model;

import com.minicad.step.model.core.base.StepEntity;
import java.util.List;
import java.util.Objects;

/**
 * Resolved MACHINING_OPERATION_SEQUENCE.
 * A machining operation sequence entity.
 *
 * @param id STEP instance id
 * @param name sequence name
 * @param operations list of machining operations
 * @param sequenceType sequence type classification
 */
/**
 * Resolved MACHINING_OPERATION_SEQUENCE.
 * A machining operation sequence entity.
 *
 * @param id STEP instance id
 * @param name sequence name
 * @param operations list of machining operations
 * @param sequenceType sequence type classification
 */
public final class StepMachiningOperationSequence implements StepEntity {
    private final int id;
    private final String name;
    private final List<StepEntity> operations;
    private final String sequenceType;

    public StepMachiningOperationSequence(int id, String name, List<StepEntity> operations, String sequenceType) {
        this.id = id;
        this.name = name;
        this.operations = operations == null ? null : java.util.List.copyOf(operations);
        this.sequenceType = sequenceType;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<StepEntity> getOperations() {
        return operations;
    }

    public String getSequenceType() {
        return sequenceType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepMachiningOperationSequence that = (StepMachiningOperationSequence) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(operations, that.operations) && Objects.equals(sequenceType, that.sequenceType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, operations, sequenceType);
    }

    @Override
    public String toString() {
        return "StepMachiningOperationSequence{" + "id=" + id + "name=" + name + "operations=" + operations + "sequenceType=" + sequenceType + "}";
    }
}