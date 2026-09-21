package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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
public final class StepPartitionInstance extends AbstractStepEntity {
    private final StepEntity partitionDefinition;
    private final String partitionState;
    private final long partitionSize;
    private final long partitionEntries;
    private final String partitionStatus;

    public StepPartitionInstance(int id, String name, StepEntity partitionDefinition, String partitionState, long partitionSize, long partitionEntries, String partitionStatus) {
        super(id, name);
        this.partitionDefinition = partitionDefinition;
        this.partitionState = partitionState;
        this.partitionSize = partitionSize;
        this.partitionEntries = partitionEntries;
        this.partitionStatus = partitionStatus;
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
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("partitionDefinition", partitionDefinition);
        state.put("partitionState", partitionState);
        state.put("partitionSize", partitionSize);
        state.put("partitionEntries", partitionEntries);
        state.put("partitionStatus", partitionStatus);
        return state;
    }
}
