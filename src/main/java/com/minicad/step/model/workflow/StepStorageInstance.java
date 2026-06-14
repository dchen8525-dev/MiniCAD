package com.minicad.step.model.workflow;

import com.minicad.step.model.base.StepEntity;
import java.util.List;
import java.util.Objects;

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
public final class StepStorageInstance implements StepEntity {
    private final int id;
    private final String name;
    private final StepEntity storageDefinition;
    private final String storageState;
    private final long storageUsed;
    private final long storageAvailable;
    private final String storageStatus;

    public StepStorageInstance(int id, String name, StepEntity storageDefinition, String storageState, long storageUsed, long storageAvailable, String storageStatus) {
        this.id = id;
        this.name = name;
        this.storageDefinition = storageDefinition;
        this.storageState = storageState;
        this.storageUsed = storageUsed;
        this.storageAvailable = storageAvailable;
        this.storageStatus = storageStatus;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepStorageInstance that = (StepStorageInstance) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(storageDefinition, that.storageDefinition) && Objects.equals(storageState, that.storageState) && storageUsed == that.storageUsed && storageAvailable == that.storageAvailable && Objects.equals(storageStatus, that.storageStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, storageDefinition, storageState, storageUsed, storageAvailable, storageStatus);
    }

    @Override
    public String toString() {
        return "StepStorageInstance{" + "id=" + id + "name=" + name + "storageDefinition=" + storageDefinition + "storageState=" + storageState + "storageUsed=" + storageUsed + "storageAvailable=" + storageAvailable + "storageStatus=" + storageStatus + "}";
    }
}