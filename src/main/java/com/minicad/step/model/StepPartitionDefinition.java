package com.minicad.step.model.workflow;

import com.minicad.step.model.core.base.StepEntity;
import java.util.List;
import java.util.Objects;

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
public final class StepPartitionDefinition implements StepEntity {
    private final int id;
    private final String name;
    private final String partitionType;
    private final String partitionCriteria;
    private final List<String> partitionRange;
    private final String partitionStatus;

    public StepPartitionDefinition(int id, String name, String partitionType, String partitionCriteria, List<String> partitionRange, String partitionStatus) {
        this.id = id;
        this.name = name;
        this.partitionType = partitionType;
        this.partitionCriteria = partitionCriteria;
        this.partitionRange = partitionRange == null ? null : java.util.List.copyOf(partitionRange);
        this.partitionStatus = partitionStatus;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepPartitionDefinition that = (StepPartitionDefinition) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(partitionType, that.partitionType) && Objects.equals(partitionCriteria, that.partitionCriteria) && Objects.equals(partitionRange, that.partitionRange) && Objects.equals(partitionStatus, that.partitionStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, partitionType, partitionCriteria, partitionRange, partitionStatus);
    }

    @Override
    public String toString() {
        return "StepPartitionDefinition{" + "id=" + id + "name=" + name + "partitionType=" + partitionType + "partitionCriteria=" + partitionCriteria + "partitionRange=" + partitionRange + "partitionStatus=" + partitionStatus + "}";
    }
}