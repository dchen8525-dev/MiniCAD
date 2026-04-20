package com.minicad.step.model.workflow;

import com.minicad.step.model.base.StepEntity;
import java.util.List;

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
public record StepPartitionInstance(
    int id,
    String name,
    StepEntity partitionDefinition,
    String partitionState,
    long partitionSize,
    long partitionEntries,
    String partitionStatus) implements StepEntity {
}