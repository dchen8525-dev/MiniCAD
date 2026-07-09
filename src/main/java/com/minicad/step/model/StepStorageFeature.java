package com.minicad.step.model.manufacturing;

import com.minicad.step.model.core.base.StepEntity;
import java.util.List;
import java.util.Objects;

/**
 * Resolved STORAGE_FEATURE.
 * A storage feature entity.
 *
 * @param id STEP instance id
 * @param name storage name
 * @param storageType storage type (rack, shelf, bin, cabinet)
 * @param storageGeometry storage geometry representation
 * @varianceCapacity storage variance capacity
 * @param storageDimensions storage dimensions
 * @param storageLocation storage location placement
 * @param storageEnvironment storage environment specification
 */
/**
 * Resolved STORAGE_FEATURE.
 * A storage feature entity.
 *
 * @param id STEP instance id
 * @param name storage name
 * @param storageType storage type (rack, shelf, bin, cabinet)
 * @param storageGeometry storage geometry representation
 * @varianceCapacity storage variance capacity
 * @param storageDimensions storage dimensions
 * @param storageLocation storage location placement
 * @param storageEnvironment storage environment specification
 */
public final class StepStorageFeature implements StepEntity {
    private final int id;
    private final String name;
    private final String storageType;
    private final StepEntity storageGeometry;
    private final int varianceCapacity;
    private final List<Double> storageDimensions;
    private final StepEntity storageLocation;
    private final String storageEnvironment;

    public StepStorageFeature(int id, String name, String storageType, StepEntity storageGeometry, int varianceCapacity, List<Double> storageDimensions, StepEntity storageLocation, String storageEnvironment) {
        this.id = id;
        this.name = name;
        this.storageType = storageType;
        this.storageGeometry = storageGeometry;
        this.varianceCapacity = varianceCapacity;
        this.storageDimensions = storageDimensions == null ? null : java.util.List.copyOf(storageDimensions);
        this.storageLocation = storageLocation;
        this.storageEnvironment = storageEnvironment;
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

    public StepEntity getStorageGeometry() {
        return storageGeometry;
    }

    public int getVarianceCapacity() {
        return varianceCapacity;
    }

    public List<Double> getStorageDimensions() {
        return storageDimensions;
    }

    public StepEntity getStorageLocation() {
        return storageLocation;
    }

    public String getStorageEnvironment() {
        return storageEnvironment;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepStorageFeature that = (StepStorageFeature) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(storageType, that.storageType) && Objects.equals(storageGeometry, that.storageGeometry) && varianceCapacity == that.varianceCapacity && Objects.equals(storageDimensions, that.storageDimensions) && Objects.equals(storageLocation, that.storageLocation) && Objects.equals(storageEnvironment, that.storageEnvironment);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, storageType, storageGeometry, varianceCapacity, storageDimensions, storageLocation, storageEnvironment);
    }

    @Override
    public String toString() {
        return "StepStorageFeature{" + "id=" + id + "name=" + name + "storageType=" + storageType + "storageGeometry=" + storageGeometry + "varianceCapacity=" + varianceCapacity + "storageDimensions=" + storageDimensions + "storageLocation=" + storageLocation + "storageEnvironment=" + storageEnvironment + "}";
    }
}