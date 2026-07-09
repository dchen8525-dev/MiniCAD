package com.minicad.step.model;

import com.minicad.step.model.StepEntity;
import java.util.List;
import java.util.Objects;

/**
 * Resolved PARTITION_INSTANCE.
 * A partition instance entity.
 *
 * @param id STEP instance id
 * @param name partition instance name
 * @param partitionDefinition partition variance definition reference
 * @param partitionState partition variance state
 * @param partitionSize partition variance size
 * @param partitionEntries partition variance entry count
 * @param partitionStatus partition variance status
 */
/**
 * Resolved PARTITION_INSTANCE.
 * A partition instance entity.
 *
 * @param id STEP instance id
 * @param name partition instance name
 * @param partitionDefinition partition variance definition reference
 * @param partitionState partition variance state
 * @param partitionSize partition variance size
 * @param partitionEntries partition variance entry count
 * @param partitionStatus partition variance status
 */
public final class StepPartitionInstance implements StepEntity {
    private final int id;
    private final String name;
    private final StepEntity partitionDefinition;
    private final String partitionState;
    private final long partitionSize;
    private final long partitionEntries;
    private final String partitionStatus;

    public StepPartitionInstance(int id, String name, StepEntity partitionDefinition, String partitionState, long partitionSize, long partitionEntries, String partitionStatus) {
        this.id = id;
        this.name = name;
        this.partitionDefinition = partitionDefinition;
        this.partitionState = partitionState;
        this.partitionSize = partitionSize;
        this.partitionEntries = partitionEntries;
        this.partitionStatus = partitionStatus;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public StepEntity getPartitionDefinition() {
        return partitionDefinition;
    }

    public String getPartitionState() {
        return partitionState;
    }

    public long getPartitionSize() {
        return partitionSize;
    }

    public long getPartitionEntries() {
        return partitionEntries;
    }

    public String getPartitionStatus() {
        return partitionStatus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepPartitionInstance that = (StepPartitionInstance) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(partitionDefinition, that.partitionDefinition) && Objects.equals(partitionState, that.partitionState) && partitionSize == that.partitionSize && partitionEntries == that.partitionEntries && Objects.equals(partitionStatus, that.partitionStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, partitionDefinition, partitionState, partitionSize, partitionEntries, partitionStatus);
    }

    @Override
    public String toString() {
        return "StepPartitionInstance{" + "id=" + id + "name=" + name + "partitionDefinition=" + partitionDefinition + "partitionState=" + partitionState + "partitionSize=" + partitionSize + "partitionEntries=" + partitionEntries + "partitionStatus=" + partitionStatus + "}";
    }
}