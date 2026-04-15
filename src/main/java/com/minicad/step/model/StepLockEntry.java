package com.minicad.step.model;

import java.util.List;

/**
 * Resolved LOCK_ENTRY.
 * A lock entry entity.
 *
 * @param id STEP instance id
 * @param name entry name
 * @param entryType entry variance type
 * @param entryTarget entry variance target reference
 * @param entryHolder entry variance holder reference
 * @param entryState entry variance state
 * @param entryTimestamp entry variance timestamp
 * @param entryStatus entry variance status
 */
public record StepLockEntry(
    int id,
    String name,
    String entryType,
    StepEntity entryTarget,
    StepEntity entryHolder,
    String entryState,
    StepEntity entryTimestamp,
    String entryStatus) implements StepEntity {
}