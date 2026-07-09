package com.minicad.step.model;

import com.minicad.step.model.core.base.StepEntity;
import java.util.List;
import java.util.Objects;

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
public final class StepPerformanceEntry implements StepEntity {
    private final int id;
    private final String name;
    private final String entryType;
    private final String entryMetric;
    private final double entryValue;
    private final StepEntity entryUnit;
    private final StepEntity entryTimestamp;
    private final StepEntity entryContext;
    private final String entryStatus;

    public StepPerformanceEntry(int id, String name, String entryType, String entryMetric, double entryValue, StepEntity entryUnit, StepEntity entryTimestamp, StepEntity entryContext, String entryStatus) {
        this.id = id;
        this.name = name;
        this.entryType = entryType;
        this.entryMetric = entryMetric;
        this.entryValue = entryValue;
        this.entryUnit = entryUnit;
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

    public String getEntryMetric() {
        return entryMetric;
    }

    public double getEntryValue() {
        return entryValue;
    }

    public StepEntity getEntryUnit() {
        return entryUnit;
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
        StepPerformanceEntry that = (StepPerformanceEntry) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(entryType, that.entryType) && Objects.equals(entryMetric, that.entryMetric) && entryValue == that.entryValue && Objects.equals(entryUnit, that.entryUnit) && Objects.equals(entryTimestamp, that.entryTimestamp) && Objects.equals(entryContext, that.entryContext) && Objects.equals(entryStatus, that.entryStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, entryType, entryMetric, entryValue, entryUnit, entryTimestamp, entryContext, entryStatus);
    }

    @Override
    public String toString() {
        return "StepPerformanceEntry{" + "id=" + id + "name=" + name + "entryType=" + entryType + "entryMetric=" + entryMetric + "entryValue=" + entryValue + "entryUnit=" + entryUnit + "entryTimestamp=" + entryTimestamp + "entryContext=" + entryContext + "entryStatus=" + entryStatus + "}";
    }
}