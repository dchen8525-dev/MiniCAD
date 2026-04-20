package com.minicad.step.model.log_audit;

import com.minicad.step.model.base.StepEntity;
import java.util.List;

/**
 * Resolved METRIC_ENTRY.
 * A metric entry entity.
 *
 * @param id STEP instance id
 * @param name entry name
 * @param entryType entry variance type
 * @param entryName entry variance metric name
 * @param entryValue entry variance metric value
 * @param entryTimestamp entry variance timestamp
 * @param entryTags entry variance tags
 * @param entryStatus entry variance status
 */
public record StepMetricEntry(
    int id,
    String name,
    String entryType,
    String entryName,
    double entryValue,
    StepEntity entryTimestamp,
    List<String> entryTags,
    String entryStatus) implements StepEntity {
}