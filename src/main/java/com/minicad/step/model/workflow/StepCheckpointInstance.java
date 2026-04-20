package com.minicad.step.model.workflow;

import com.minicad.step.model.base.StepEntity;
import java.util.List;

/**
 * Resolved CHECKPOINT_INSTANCE.
 * A checkpoint instance entity.
 *
 * @param id STEP instance id
 * @param name checkpoint instance name
 * @param checkpointDefinition checkpoint variance definition reference
 * @param checkpointTime checkpoint variance creation time
 * @param checkpointSize checkpoint variance size
 * @param checkpointValid checkpoint variance valid flag
 * @param checkpointStatus checkpoint variance status
 */
public record StepCheckpointInstance(
    int id,
    String name,
    StepEntity checkpointDefinition,
    StepEntity checkpointTime,
    long checkpointSize,
    boolean checkpointValid,
    String checkpointStatus) implements StepEntity {
}