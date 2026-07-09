package com.minicad.step.model.validation;

import com.minicad.step.model.core.base.StepEntity;
import java.util.List;
import java.util.Objects;

/**
 * Resolved TRACE_ENTRY.
 * A trace entry entity.
 *
 * @param id STEP instance id
 * @param name entry name
 * @param entryType entry variance type
 * @param entryOperation entry variance operation name
 * @param entryDetails entry variance trace details
 * @param entryTimestamp entry variance timestamp
 * @param entryDuration entry variance duration
 * @param entryStatus entry variance status
 */
/**
 * Resolved TRACE_ENTRY.
 * A trace entry entity.
 *
 * @param id STEP instance id
 * @param name entry name
 * @param entryType entry variance type
 * @param entryOperation entry variance operation name
 * @param entryDetails entry variance trace details
 * @param entryTimestamp entry variance timestamp
 * @param entryDuration entry variance duration
 * @param entryStatus entry variance status
 */
public final class StepTraceEntry implements StepEntity {
    private final int id;
    private final String name;
    private final String entryType;
    private final String entryOperation;
    private final List<String> entryDetails;
    private final StepEntity entryTimestamp;
    private final long entryDuration;
    private final String entryStatus;

    public StepTraceEntry(int id, String name, String entryType, String entryOperation, List<String> entryDetails, StepEntity entryTimestamp, long entryDuration, String entryStatus) {
        this.id = id;
        this.name = name;
        this.entryType = entryType;
        this.entryOperation = entryOperation;
        this.entryDetails = entryDetails == null ? null : java.util.List.copyOf(entryDetails);
        this.entryTimestamp = entryTimestamp;
        this.entryDuration = entryDuration;
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

    public String getEntryOperation() {
        return entryOperation;
    }

    public List<String> getEntryDetails() {
        return entryDetails;
    }

    public StepEntity getEntryTimestamp() {
        return entryTimestamp;
    }

    public long getEntryDuration() {
        return entryDuration;
    }

    public String getEntryStatus() {
        return entryStatus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepTraceEntry that = (StepTraceEntry) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(entryType, that.entryType) && Objects.equals(entryOperation, that.entryOperation) && Objects.equals(entryDetails, that.entryDetails) && Objects.equals(entryTimestamp, that.entryTimestamp) && entryDuration == that.entryDuration && Objects.equals(entryStatus, that.entryStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, entryType, entryOperation, entryDetails, entryTimestamp, entryDuration, entryStatus);
    }

    @Override
    public String toString() {
        return "StepTraceEntry{" + "id=" + id + "name=" + name + "entryType=" + entryType + "entryOperation=" + entryOperation + "entryDetails=" + entryDetails + "entryTimestamp=" + entryTimestamp + "entryDuration=" + entryDuration + "entryStatus=" + entryStatus + "}";
    }
}