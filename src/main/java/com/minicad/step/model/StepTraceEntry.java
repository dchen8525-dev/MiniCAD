package com.minicad.step.model;

import java.util.List;

/**
 * Resolved TRACE_ENTRY.
 * A trace entry entity.
 *
 * @param id STEP instance id
 * @param name entry name
 * @param entryType entry variance type
 * @param entryOperation entry variance operation name
 * @param entryDetails entry variance trace details
 * @param entryTimestamp entry variance timestamp
 * @param entryDuration entry variance duration
 * @param entryStatus entry variance status
 */
public record StepTraceEntry(
    int id,
    String name,
    String entryType,
    String entryOperation,
    List<String> entryDetails,
    StepEntity entryTimestamp,
    long entryDuration,
    String entryStatus) implements StepEntity {
}