package com.minicad.step.model;

import java.util.List;

/**
 * Resolved WARNING_ENTRY.
 * A warning entry entity.
 *
 * @param id STEP instance id
 * @param name entry name
 * @param entryType entry variance type
 * @param entryCode entry variance warning code
 * @param entryMessage entry variance warning message
 * @param entrySeverity entry variance severity level
 * @param entryTimestamp entry variance timestamp
 * @param entryContext entry variance context reference
 * @param entryStatus entry variance status
 */
public record StepWarningEntry(
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