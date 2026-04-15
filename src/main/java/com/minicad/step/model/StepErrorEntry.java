package com.minicad.step.model;

import java.util.List;

/**
 * Resolved ERROR_ENTRY.
 * An error entry entity.
 *
 * @param id STEP instance id
 * @param name entry name
 * @param entryType entry variance type
 * @param entryCode entry variance error code
 * @param entryMessage entry variance error message
 * @param entrySeverity entry variance severity level
 * @param entryTimestamp entry variance timestamp
 * @param entryContext entry variance context reference
 * @param entryStatus entry variance status
 */
public record StepErrorEntry(
    int id,
    String name,
    String entryType,
    String entryCode,
    String entryMessage,
    int entrySeverity,
    StepEntity entryTimestamp,
    StepEntity entryContext,
    String entryStatus) implements StepEntity {
}