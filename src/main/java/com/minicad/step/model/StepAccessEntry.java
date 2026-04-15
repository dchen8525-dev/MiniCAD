package com.minicad.step.model;

import java.util.List;

/**
 * Resolved ACCESS_ENTRY.
 * An access entry entity.
 *
 * @param id STEP instance id
 * @param name entry name
 * @param entryType entry variance type
 * @param entryAction entry variance action (read/write/delete)
 * @param entryTarget entry variance target reference
 * @param entryActor entry variance actor reference
 * @param entryTimestamp entry variance timestamp
 * @param entryResult entry variance result
 * @param entryStatus entry variance status
 */
public record StepAccessEntry(
    int id,
    String name,
    String entryType,
    String entryAction,
    StepEntity entryTarget,
    StepEntity entryActor,
    StepEntity entryTimestamp,
    String entryResult,
    String entryStatus) implements StepEntity {
}