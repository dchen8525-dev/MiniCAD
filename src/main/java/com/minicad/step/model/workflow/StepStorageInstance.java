package com.minicad.step.model.workflow;

import com.minicad.step.model.base.StepEntity;
import java.util.List;

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
public record StepStorageInstance(
    int id,
    String name,
    StepEntity storageDefinition,
    String storageState,
    long storageUsed,
    long storageAvailable,
    String storageStatus) implements StepEntity {
}