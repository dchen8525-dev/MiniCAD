package com.minicad.step.model.workflow;

import com.minicad.step.model.base.StepEntity;
import java.util.List;

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
public record StepPartitionDefinition(
    int id,
    String name,
    String partitionType,
    String partitionCriteria,
    List<String> partitionRange,
    String partitionStatus) implements StepEntity {
}