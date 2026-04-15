package com.minicad.step.model;

import java.util.List;

/**
 * Resolved LOCK_DEFINITION.
 * A lock definition entity.
 *
 * @param id STEP instance id
 * @param name lock name
 * @param lockType lock variance type
 * @param lockScope lock variance scope
 * @param lockTimeout lock variance timeout in seconds
 * @param lockPolicy lock variance policy
 * @param lockStatus lock variance status
 */
public record StepLockDefinition(
    int id,
    String name,
    String lockType,
    String lockScope,
    int lockTimeout,
    String lockPolicy,
    String lockStatus) implements StepEntity {
}