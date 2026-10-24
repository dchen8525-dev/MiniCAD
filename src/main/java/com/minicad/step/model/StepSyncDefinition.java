package com.minicad.step.model;

import java.util.List;
import java.util.Map;

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
public final class StepSyncDefinition extends AbstractStepEntity {
    private final String syncType;
    private final String syncDirection;
    private final int syncInterval;
    private final String syncConflictResolution;
    private final String syncStatus;

    public StepSyncDefinition(int id, String name, String syncType, String syncDirection, int syncInterval, String syncConflictResolution, String syncStatus) {
        super(id, name);
        this.syncType = syncType;
        this.syncDirection = syncDirection;
        this.syncInterval = syncInterval;
        this.syncConflictResolution = syncConflictResolution;
        this.syncStatus = syncStatus;
    }

    public String getSyncType() {
        return syncType;
    }

    public String getSyncDirection() {
        return syncDirection;
    }

    public int getSyncInterval() {
        return syncInterval;
    }

    public String getSyncConflictResolution() {
        return syncConflictResolution;
    }

    public String getSyncStatus() {
        return syncStatus;
    }

    @Override
    protected Map<String, Object> components() {
        return componentsOf(
                "id", getId(), "name", getName(), "syncType", syncType, "syncDirection", syncDirection,
                "syncInterval", syncInterval, "syncConflictResolution", syncConflictResolution,
                "syncStatus", syncStatus);
    }
}
