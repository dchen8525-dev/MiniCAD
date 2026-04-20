package com.minicad.step.model.validation;

import com.minicad.step.model.base.StepEntity;
import java.util.List;

/**
 * Resolved SUMMARY_ENTRY.
 * A summary entry entity.
 *
 * @param id STEP instance id
 * @param name entry name
 * @param entryType entry variance type
 * @param entrySummary entry variance summary text
 * @param entryHighlights entry variance highlights
 * @param entryTimestamp entry variance timestamp
 * @param entryContext entry variance context reference
 * @param entryStatus entry variance status
 */
public record StepSummaryEntry(
    int id,
    String name,
    String entryType,
    String entrySummary,
    List<String> entryHighlights,
    StepEntity entryTimestamp,
    StepEntity entryContext,
    String entryStatus) implements StepEntity {
}