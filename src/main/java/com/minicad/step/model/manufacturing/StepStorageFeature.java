package com.minicad.step.model.manufacturing;

import com.minicad.step.model.base.StepEntity;
import java.util.List;

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
public record StepStorageFeature(
    int id,
    String name,
    String storageType,
    StepEntity storageGeometry,
    int varianceCapacity,
    List<Double> storageDimensions,
    StepEntity storageLocation,
    String storageEnvironment) implements StepEntity {
}