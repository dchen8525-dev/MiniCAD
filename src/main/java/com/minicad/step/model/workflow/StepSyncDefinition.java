package com.minicad.step.model.workflow;

import com.minicad.step.model.base.StepEntity;
import java.util.List;

/**
 * Resolved SYNC_DEFINITION.
 * A sync definition entity.
 *
 * @param id STEP instance id
 * @param name sync name
 * @param syncType sync variance type
 * @param syncDirection sync variance direction
 * @param syncInterval sync variance interval
 * @param syncConflictResolution sync variance conflict resolution policy
 * @param syncStatus sync variance status
 */
public record StepSyncDefinition(
    int id,
    String name,
    String syncType,
    String syncDirection,
    int syncInterval,
    String syncConflictResolution,
    String syncStatus) implements StepEntity {
}