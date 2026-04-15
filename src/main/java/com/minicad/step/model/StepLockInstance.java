package com.minicad.step.model;

import java.util.List;

/**
 * Resolved LOCK_INSTANCE.
 * A lock instance entity.
 *
 * @param id STEP instance id
 * @param name lock instance name
 * @param lockDefinition lock variance definition reference
 * @param lockState lock variance state
 * @param lockHolder lock variance holder reference
 * @param lockAcquiredTime lock variance acquired time
 * @param lockStatus lock variance status
 */
public record StepLockInstance(
    int id,
    String name,
    StepEntity lockDefinition,
    String lockState,
    StepEntity lockHolder,
    StepEntity lockAcquiredTime,
    String lockStatus) implements StepEntity {
}