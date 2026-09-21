package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Resolved STORAGE_DEFINITION.
 * A storage definition entity.
 *
 * @param id STEP instance id
 * @param name storage name
 * @param storageType storage variance type
 * @param storageCapacity storage variance capacity
 * @param storageFormat storage variance format
 * @param storageLocation storage variance location reference
 * @param storageStatus storage variance status
 */
public final class StepStorageDefinition extends AbstractStepEntity {
    private final String storageType;
    private final long storageCapacity;
    private final String storageFormat;
    private final StepEntity storageLocation;
    private final String storageStatus;

    public StepStorageDefinition(int id, String name, String storageType, long storageCapacity, String storageFormat, StepEntity storageLocation, String storageStatus) {
        super(id, name);
        this.storageType = storageType;
        this.storageCapacity = storageCapacity;
        this.storageFormat = storageFormat;
        this.storageLocation = storageLocation;
        this.storageStatus = storageStatus;
    }

    public String getStorageType() {
        return storageType;
    }

    public long getStorageCapacity() {
        return storageCapacity;
    }

    public String getStorageFormat() {
        return storageFormat;
    }

    public StepEntity getStorageLocation() {
        return storageLocation;
    }

    public String getStorageStatus() {
        return storageStatus;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("storageType", storageType);
        state.put("storageCapacity", storageCapacity);
        state.put("storageFormat", storageFormat);
        state.put("storageLocation", storageLocation);
        state.put("storageStatus", storageStatus);
        return state;
    }
}
