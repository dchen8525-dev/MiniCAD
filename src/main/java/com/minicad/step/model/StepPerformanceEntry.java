package com.minicad.step.model;

import java.util.List;

/**
 * Resolved PERFORMANCE_ENTRY.
 * A performance entry entity.
 *
 * @param id STEP instance id
 * @param name entry name
 * @param entryType entry variance type
 * @param entryMetric entry variance metric name
 * @param entryValue entry variance metric value
 * @param entryUnit entry variance unit
 * @param entryTimestamp entry variance timestamp
 * @param entryContext entry variance context reference
 * @param entryStatus entry variance status
 */
public record StepPerformanceEntry(
    int id,
    String name,
    String entryType,
    String entryMetric,
    double entryValue,
    StepEntity entryUnit,
    StepEntity entryTimestamp,
    StepEntity entryContext,
    String entryStatus) implements StepEntity {
}