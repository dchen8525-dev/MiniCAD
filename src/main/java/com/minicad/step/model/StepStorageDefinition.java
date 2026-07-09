package com.minicad.step.model;

import com.minicad.step.model.StepEntity;
import java.util.List;
import java.util.Objects;

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
public final class StepStorageDefinition implements StepEntity {
    private final int id;
    private final String name;
    private final String storageType;
    private final long storageCapacity;
    private final String storageFormat;
    private final StepEntity storageLocation;
    private final String storageStatus;

    public StepStorageDefinition(int id, String name, String storageType, long storageCapacity, String storageFormat, StepEntity storageLocation, String storageStatus) {
        this.id = id;
        this.name = name;
        this.storageType = storageType;
        this.storageCapacity = storageCapacity;
        this.storageFormat = storageFormat;
        this.storageLocation = storageLocation;
        this.storageStatus = storageStatus;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepStorageDefinition that = (StepStorageDefinition) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(storageType, that.storageType) && storageCapacity == that.storageCapacity && Objects.equals(storageFormat, that.storageFormat) && Objects.equals(storageLocation, that.storageLocation) && Objects.equals(storageStatus, that.storageStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, storageType, storageCapacity, storageFormat, storageLocation, storageStatus);
    }

    @Override
    public String toString() {
        return "StepStorageDefinition{" + "id=" + id + "name=" + name + "storageType=" + storageType + "storageCapacity=" + storageCapacity + "storageFormat=" + storageFormat + "storageLocation=" + storageLocation + "storageStatus=" + storageStatus + "}";
    }
}