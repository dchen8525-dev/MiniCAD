package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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
public final class StepStorageFeature extends AbstractStepEntity {
    private final String storageType;
    private final StepEntity storageGeometry;
    private final int varianceCapacity;
    private final List<Double> storageDimensions;
    private final StepEntity storageLocation;
    private final String storageEnvironment;

    public StepStorageFeature(int id, String name, String storageType, StepEntity storageGeometry, int varianceCapacity, List<Double> storageDimensions, StepEntity storageLocation, String storageEnvironment) {
        super(id, name);
        this.storageType = storageType;
        this.storageGeometry = storageGeometry;
        this.varianceCapacity = varianceCapacity;
        this.storageDimensions = storageDimensions == null ? null : java.util.List.copyOf(storageDimensions);
        this.storageLocation = storageLocation;
        this.storageEnvironment = storageEnvironment;
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
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("storageType", storageType);
        state.put("storageGeometry", storageGeometry);
        state.put("varianceCapacity", varianceCapacity);
        state.put("storageDimensions", storageDimensions);
        state.put("storageLocation", storageLocation);
        state.put("storageEnvironment", storageEnvironment);
        return state;
    }
}
