package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Resolved STORAGE_INSTANCE.
 * A storage instance entity.
 *
 * @param id STEP instance id
 * @param name storage instance name
 * @param storageDefinition storage variance definition reference
 * @param storageState storage variance state
 * @param storageUsed storage variance used space
 * @param storageAvailable storage variance available space
 * @param storageStatus storage variance status
 */
public final class StepStorageInstance extends AbstractStepEntity {
    private final StepEntity storageDefinition;
    private final String storageState;
    private final long storageUsed;
    private final long storageAvailable;
    private final String storageStatus;

    public StepStorageInstance(int id, String name, StepEntity storageDefinition, String storageState, long storageUsed, long storageAvailable, String storageStatus) {
        super(id, name);
        this.storageDefinition = storageDefinition;
        this.storageState = storageState;
        this.storageUsed = storageUsed;
        this.storageAvailable = storageAvailable;
        this.storageStatus = storageStatus;
    }

    public StepEntity getStorageDefinition() {
        return storageDefinition;
    }

    public String getStorageState() {
        return storageState;
    }

    public long getStorageUsed() {
        return storageUsed;
    }

    public long getStorageAvailable() {
        return storageAvailable;
    }

    public String getStorageStatus() {
        return storageStatus;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("storageDefinition", storageDefinition);
        state.put("storageState", storageState);
        state.put("storageUsed", storageUsed);
        state.put("storageAvailable", storageAvailable);
        state.put("storageStatus", storageStatus);
        return state;
    }
}
