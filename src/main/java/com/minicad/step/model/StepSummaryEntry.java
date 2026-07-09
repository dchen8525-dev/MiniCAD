package com.minicad.step.model;

import com.minicad.step.model.StepEntity;
import java.util.List;
import java.util.Objects;

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
public final class StepSummaryEntry implements StepEntity {
    private final int id;
    private final String name;
    private final String entryType;
    private final String entrySummary;
    private final List<String> entryHighlights;
    private final StepEntity entryTimestamp;
    private final StepEntity entryContext;
    private final String entryStatus;

    public StepSummaryEntry(int id, String name, String entryType, String entrySummary, List<String> entryHighlights, StepEntity entryTimestamp, StepEntity entryContext, String entryStatus) {
        this.id = id;
        this.name = name;
        this.entryType = entryType;
        this.entrySummary = entrySummary;
        this.entryHighlights = entryHighlights == null ? null : java.util.List.copyOf(entryHighlights);
        this.entryTimestamp = entryTimestamp;
        this.entryContext = entryContext;
        this.entryStatus = entryStatus;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEntryType() {
        return entryType;
    }

    public String getEntrySummary() {
        return entrySummary;
    }

    public List<String> getEntryHighlights() {
        return entryHighlights;
    }

    public StepEntity getEntryTimestamp() {
        return entryTimestamp;
    }

    public StepEntity getEntryContext() {
        return entryContext;
    }

    public String getEntryStatus() {
        return entryStatus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepSummaryEntry that = (StepSummaryEntry) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(entryType, that.entryType) && Objects.equals(entrySummary, that.entrySummary) && Objects.equals(entryHighlights, that.entryHighlights) && Objects.equals(entryTimestamp, that.entryTimestamp) && Objects.equals(entryContext, that.entryContext) && Objects.equals(entryStatus, that.entryStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, entryType, entrySummary, entryHighlights, entryTimestamp, entryContext, entryStatus);
    }

    @Override
    public String toString() {
        return "StepSummaryEntry{" + "id=" + id + "name=" + name + "entryType=" + entryType + "entrySummary=" + entrySummary + "entryHighlights=" + entryHighlights + "entryTimestamp=" + entryTimestamp + "entryContext=" + entryContext + "entryStatus=" + entryStatus + "}";
    }
}