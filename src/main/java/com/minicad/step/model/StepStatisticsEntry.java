package com.minicad.step.model;

import java.util.List;
import java.util.Map;

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
public final class StepStatisticsEntry extends AbstractStepEntity {
    private final String entryType;
    private final String entryStatistic;
    private final List<Double> entryValues;
    private final StepEntity entryTimestamp;
    private final StepEntity entryContext;
    private final String entryStatus;

    public StepStatisticsEntry(int id, String name, String entryType, String entryStatistic, List<Double> entryValues, StepEntity entryTimestamp, StepEntity entryContext, String entryStatus) {
        super(id, name);
        this.entryType = entryType;
        this.entryStatistic = entryStatistic;
        this.entryValues = entryValues == null ? null : java.util.List.copyOf(entryValues);
        this.entryTimestamp = entryTimestamp;
        this.entryContext = entryContext;
        this.entryStatus = entryStatus;
    }

    public String getEntryType() {
        return entryType;
    }

    public String getEntryStatistic() {
        return entryStatistic;
    }

    public List<Double> getEntryValues() {
        return entryValues;
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
        return componentsOf(
                "id", getId(), "name", getName(), "entryType", entryType, "entryStatistic", entryStatistic,
                "entryValues", entryValues, "entryTimestamp", entryTimestamp, "entryContext", entryContext,
                "entryStatus", entryStatus);
    }
}
