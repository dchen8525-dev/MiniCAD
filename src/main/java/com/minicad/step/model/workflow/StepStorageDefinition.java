package com.minicad.step.model.workflow;

import com.minicad.step.model.base.StepEntity;
import java.util.List;

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
public record StepStorageDefinition(
    int id,
    String name,
    String storageType,
    long storageCapacity,
    String storageFormat,
    StepEntity storageLocation,
    String storageStatus) implements StepEntity {
}