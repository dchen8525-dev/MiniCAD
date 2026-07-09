package com.minicad.step.model;

import com.minicad.step.model.core.base.StepEntity;
import java.util.List;
import java.util.Objects;

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
public final class StepSyncDefinition implements StepEntity {
    private final int id;
    private final String name;
    private final String syncType;
    private final String syncDirection;
    private final int syncInterval;
    private final String syncConflictResolution;
    private final String syncStatus;

    public StepSyncDefinition(int id, String name, String syncType, String syncDirection, int syncInterval, String syncConflictResolution, String syncStatus) {
        this.id = id;
        this.name = name;
        this.syncType = syncType;
        this.syncDirection = syncDirection;
        this.syncInterval = syncInterval;
        this.syncConflictResolution = syncConflictResolution;
        this.syncStatus = syncStatus;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepSyncDefinition that = (StepSyncDefinition) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(syncType, that.syncType) && Objects.equals(syncDirection, that.syncDirection) && syncInterval == that.syncInterval && Objects.equals(syncConflictResolution, that.syncConflictResolution) && Objects.equals(syncStatus, that.syncStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, syncType, syncDirection, syncInterval, syncConflictResolution, syncStatus);
    }

    @Override
    public String toString() {
        return "StepSyncDefinition{" + "id=" + id + "name=" + name + "syncType=" + syncType + "syncDirection=" + syncDirection + "syncInterval=" + syncInterval + "syncConflictResolution=" + syncConflictResolution + "syncStatus=" + syncStatus + "}";
    }
}