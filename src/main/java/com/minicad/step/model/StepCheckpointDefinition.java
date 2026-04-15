package com.minicad.step.model;

import java.util.List;

/**
 * Resolved CHECKPOINT_DEFINITION.
 * A checkpoint definition entity.
 *
 * @param id STEP instance id
 * @param name checkpoint name
 * @param checkpointType checkpoint variance type
 * @param checkpointLocation checkpoint variance location reference
 * @param checkpointFrequency checkpoint variance frequency
 * @param checkpointRetention checkpoint variance retention count
 * @param checkpointStatus checkpoint variance status
 */
public record StepCheckpointDefinition(
    int id,
    String name,
    String checkpointType,
    StepEntity checkpointLocation,
    int checkpointFrequency,
    int checkpointRetention,
    String checkpointStatus) implements StepEntity {
}