package com.minicad.step.model;

import com.minicad.step.model.StepEntity;
import java.util.List;
import java.util.Objects;

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
public final class StepMetricEntry implements StepEntity {
    private final int id;
    private final String name;
    private final String entryType;
    private final String entryName;
    private final double entryValue;
    private final StepEntity entryTimestamp;
    private final List<String> entryTags;
    private final String entryStatus;

    public StepMetricEntry(int id, String name, String entryType, String entryName, double entryValue, StepEntity entryTimestamp, List<String> entryTags, String entryStatus) {
        this.id = id;
        this.name = name;
        this.entryType = entryType;
        this.entryName = entryName;
        this.entryValue = entryValue;
        this.entryTimestamp = entryTimestamp;
        this.entryTags = entryTags == null ? null : java.util.List.copyOf(entryTags);
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

    public String getEntryName() {
        return entryName;
    }

    public double getEntryValue() {
        return entryValue;
    }

    public StepEntity getEntryTimestamp() {
        return entryTimestamp;
    }

    public List<String> getEntryTags() {
        return entryTags;
    }

    public String getEntryStatus() {
        return entryStatus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepMetricEntry that = (StepMetricEntry) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(entryType, that.entryType) && Objects.equals(entryName, that.entryName) && entryValue == that.entryValue && Objects.equals(entryTimestamp, that.entryTimestamp) && Objects.equals(entryTags, that.entryTags) && Objects.equals(entryStatus, that.entryStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, entryType, entryName, entryValue, entryTimestamp, entryTags, entryStatus);
    }

    @Override
    public String toString() {
        return "StepMetricEntry{" + "id=" + id + "name=" + name + "entryType=" + entryType + "entryName=" + entryName + "entryValue=" + entryValue + "entryTimestamp=" + entryTimestamp + "entryTags=" + entryTags + "entryStatus=" + entryStatus + "}";
    }
}