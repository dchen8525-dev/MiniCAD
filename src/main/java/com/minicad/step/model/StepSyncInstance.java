package com.minicad.step.model;

import com.minicad.step.model.core.base.StepEntity;
import java.util.List;
import java.util.Objects;

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
public final class StepSyncInstance implements StepEntity {
    private final int id;
    private final String name;
    private final StepEntity syncDefinition;
    private final String syncState;
    private final StepEntity syncLastSync;
    private final int syncPending;
    private final int syncConflicts;
    private final String syncStatus;

    public StepSyncInstance(int id, String name, StepEntity syncDefinition, String syncState, StepEntity syncLastSync, int syncPending, int syncConflicts, String syncStatus) {
        this.id = id;
        this.name = name;
        this.syncDefinition = syncDefinition;
        this.syncState = syncState;
        this.syncLastSync = syncLastSync;
        this.syncPending = syncPending;
        this.syncConflicts = syncConflicts;
        this.syncStatus = syncStatus;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public StepEntity getSyncDefinition() {
        return syncDefinition;
    }

    public String getSyncState() {
        return syncState;
    }

    public StepEntity getSyncLastSync() {
        return syncLastSync;
    }

    public int getSyncPending() {
        return syncPending;
    }

    public int getSyncConflicts() {
        return syncConflicts;
    }

    public String getSyncStatus() {
        return syncStatus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepSyncInstance that = (StepSyncInstance) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(syncDefinition, that.syncDefinition) && Objects.equals(syncState, that.syncState) && Objects.equals(syncLastSync, that.syncLastSync) && syncPending == that.syncPending && syncConflicts == that.syncConflicts && Objects.equals(syncStatus, that.syncStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, syncDefinition, syncState, syncLastSync, syncPending, syncConflicts, syncStatus);
    }

    @Override
    public String toString() {
        return "StepSyncInstance{" + "id=" + id + "name=" + name + "syncDefinition=" + syncDefinition + "syncState=" + syncState + "syncLastSync=" + syncLastSync + "syncPending=" + syncPending + "syncConflicts=" + syncConflicts + "syncStatus=" + syncStatus + "}";
    }
}