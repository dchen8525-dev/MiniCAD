package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Resolved PARTITION_DEFINITION.
 * A partition definition entity.
 *
 * @param id STEP instance id
 * @param name partition name
 * @param partitionType partition variance type
 * @param partitionCriteria partition variance criteria
 * @param partitionRange partition variance range
 * @param partitionStatus partition variance status
 */
public final class StepPartitionDefinition extends AbstractStepEntity {
    private final String partitionType;
    private final String partitionCriteria;
    private final List<String> partitionRange;
    private final String partitionStatus;

    public StepPartitionDefinition(int id, String name, String partitionType, String partitionCriteria, List<String> partitionRange, String partitionStatus) {
        super(id, name);
        this.partitionType = partitionType;
        this.partitionCriteria = partitionCriteria;
        this.partitionRange = partitionRange == null ? null : java.util.List.copyOf(partitionRange);
        this.partitionStatus = partitionStatus;
    }

    public String getPartitionType() {
        return partitionType;
    }

    public String getPartitionCriteria() {
        return partitionCriteria;
    }

    public List<String> getPartitionRange() {
        return partitionRange;
    }

    public String getPartitionStatus() {
        return partitionStatus;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("partitionType", partitionType);
        state.put("partitionCriteria", partitionCriteria);
        state.put("partitionRange", partitionRange);
        state.put("partitionStatus", partitionStatus);
        return state;
    }
}
