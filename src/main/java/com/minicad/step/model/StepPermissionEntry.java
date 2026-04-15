package com.minicad.step.model;

import java.util.List;

/**
 * Resolved PERMISSION_ENTRY.
 * A permission entry entity.
 *
 * @param id STEP instance id
 * @param name entry name
 * @param entryType entry variance type
 * @param entryPermission entry variance permission
 * @param entryTarget entry variance target reference
 * @param entryHolder entry variance holder reference
 * @param entryGranted entry variance granted flag
 * @param entryTimestamp entry variance timestamp
 * @param entryStatus entry variance status
 */
public record StepPermissionEntry(
    int id,
    String name,
    String entryType,
    String entryPermission,
    StepEntity entryTarget,
    StepEntity entryHolder,
    boolean entryGranted,
    StepEntity entryTimestamp,
    String entryStatus) implements StepEntity {
}