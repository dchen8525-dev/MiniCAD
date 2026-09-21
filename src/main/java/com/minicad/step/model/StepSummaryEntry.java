package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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
public final class StepSummaryEntry extends AbstractStepEntity {
    private final String entryType;
    private final String entrySummary;
    private final List<String> entryHighlights;
    private final StepEntity entryTimestamp;
    private final StepEntity entryContext;
    private final String entryStatus;

    public StepSummaryEntry(int id, String name, String entryType, String entrySummary, List<String> entryHighlights, StepEntity entryTimestamp, StepEntity entryContext, String entryStatus) {
        super(id, name);
        this.entryType = entryType;
        this.entrySummary = entrySummary;
        this.entryHighlights = entryHighlights == null ? null : java.util.List.copyOf(entryHighlights);
        this.entryTimestamp = entryTimestamp;
        this.entryContext = entryContext;
        this.entryStatus = entryStatus;
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
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("entryType", entryType);
        state.put("entrySummary", entrySummary);
        state.put("entryHighlights", entryHighlights);
        state.put("entryTimestamp", entryTimestamp);
        state.put("entryContext", entryContext);
        state.put("entryStatus", entryStatus);
        return state;
    }
}
