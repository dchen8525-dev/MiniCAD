package com.minicad.step.model;

import java.util.List;

/**
 * Resolved LOG_ENTRY.
 * A log entry entity.
 *
 * @param id STEP instance id
 * @param name entry name
 * @param entryType entry variance type
 * @param entryLevel entry variance level
 * @param entryMessage entry variance message
 * @param entryTimestamp entry variance timestamp
 * @param entryContext entry variance context reference
 * @param entryStatus entry variance status
 */
public record StepLogEntry(
    int id,
    String name,
    String entryType,
    String entryLevel,
    String entryMessage,
    StepEntity entryTimestamp,
    StepEntity entryContext,
    String entryStatus) implements StepEntity {
}