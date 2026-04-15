package com.minicad.step.model;

import java.util.List;

/**
 * Resolved LOCK_RECORD.
 * A lock record entity.
 *
 * @param id STEP instance id
 * @param name lock name
 * @param lockType lock variance type
 * @param lockTarget lock variance target reference
 * @param lockHolder lock variance holder reference
 * @param lockAcquiredTime lock variance acquired time
 * @param lockExpiresTime lock variance expires time
 * @param lockStatus lock variance status
 */
public record StepLockRecord(
    int id,
    String name,
    String lockType,
    StepEntity lockTarget,
    StepEntity lockHolder,
    StepEntity lockAcquiredTime,
    StepEntity lockExpiresTime,
    String lockStatus) implements StepEntity {
}