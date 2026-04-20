package com.minicad.step.model.workflow;

import com.minicad.step.model.base.StepEntity;
import java.util.List;

/**
 * Resolved SYNC_INSTANCE.
 * A sync instance entity.
 *
 * @param id STEP instance id
 * @param name sync instance name
 * @param syncDefinition sync variance definition reference
 * @param syncState sync variance state
 * @param syncLastSync sync variance last sync time
 * @param syncPending sync variance pending changes
 * @param syncConflicts sync variance conflict count
 * @param syncStatus sync variance status
 */
public record StepSyncInstance(
    int id,
    String name,
    StepEntity syncDefinition,
    String syncState,
    StepEntity syncLastSync,
    int syncPending,
    int syncConflicts,
    String syncStatus) implements StepEntity {
}