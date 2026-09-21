package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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
public final class StepSyncInstance extends AbstractStepEntity {
    private final StepEntity syncDefinition;
    private final String syncState;
    private final StepEntity syncLastSync;
    private final int syncPending;
    private final int syncConflicts;
    private final String syncStatus;

    public StepSyncInstance(int id, String name, StepEntity syncDefinition, String syncState, StepEntity syncLastSync, int syncPending, int syncConflicts, String syncStatus) {
        super(id, name);
        this.syncDefinition = syncDefinition;
        this.syncState = syncState;
        this.syncLastSync = syncLastSync;
        this.syncPending = syncPending;
        this.syncConflicts = syncConflicts;
        this.syncStatus = syncStatus;
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
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("syncDefinition", syncDefinition);
        state.put("syncState", syncState);
        state.put("syncLastSync", syncLastSync);
        state.put("syncPending", syncPending);
        state.put("syncConflicts", syncConflicts);
        state.put("syncStatus", syncStatus);
        return state;
    }
}
