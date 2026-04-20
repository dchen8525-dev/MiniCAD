package com.minicad.step.model.log_audit;

import com.minicad.step.model.base.StepEntity;
import java.util.List;

/**
 * Resolved STATISTICS_ENTRY.
 * A statistics entry entity.
 *
 * @param id STEP instance id
 * @param name entry name
 * @param entryType entry variance type
 * @param entryStatistic entry variance statistic type
 * @param entryValues entry variance values
 * @param entryTimestamp entry variance timestamp
 * @param entryContext entry variance context reference
 * @param entryStatus entry variance status
 */
public record StepStatisticsEntry(
    int id,
    String name,
    String entryType,
    String entryStatistic,
    List<Double> entryValues,
    StepEntity entryTimestamp,
    StepEntity entryContext,
    String entryStatus) implements StepEntity {
}