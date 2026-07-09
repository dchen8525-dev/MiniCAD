package com.minicad.step.model;

import com.minicad.step.model.core.base.StepEntity;
import java.util.List;
import java.util.Objects;

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
public final class StepStatisticsEntry implements StepEntity {
    private final int id;
    private final String name;
    private final String entryType;
    private final String entryStatistic;
    private final List<Double> entryValues;
    private final StepEntity entryTimestamp;
    private final StepEntity entryContext;
    private final String entryStatus;

    public StepStatisticsEntry(int id, String name, String entryType, String entryStatistic, List<Double> entryValues, StepEntity entryTimestamp, StepEntity entryContext, String entryStatus) {
        this.id = id;
        this.name = name;
        this.entryType = entryType;
        this.entryStatistic = entryStatistic;
        this.entryValues = entryValues == null ? null : java.util.List.copyOf(entryValues);
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepStatisticsEntry that = (StepStatisticsEntry) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(entryType, that.entryType) && Objects.equals(entryStatistic, that.entryStatistic) && Objects.equals(entryValues, that.entryValues) && Objects.equals(entryTimestamp, that.entryTimestamp) && Objects.equals(entryContext, that.entryContext) && Objects.equals(entryStatus, that.entryStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, entryType, entryStatistic, entryValues, entryTimestamp, entryContext, entryStatus);
    }

    @Override
    public String toString() {
        return "StepStatisticsEntry{" + "id=" + id + "name=" + name + "entryType=" + entryType + "entryStatistic=" + entryStatistic + "entryValues=" + entryValues + "entryTimestamp=" + entryTimestamp + "entryContext=" + entryContext + "entryStatus=" + entryStatus + "}";
    }
}