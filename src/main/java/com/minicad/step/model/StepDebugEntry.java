package com.minicad.step.model;

import com.minicad.step.model.core.base.StepEntity;
import java.util.List;
import java.util.Objects;

/**
 * Resolved DEBUG_ENTRY.
 * A debug entry entity.
 *
 * @param id STEP instance id
 * @param name entry name
 * @param entryType entry variance type
 * @param entryMessage entry variance debug message
 * @param entryDetails entry variance debug details
 * @param entryTimestamp entry variance timestamp
 * @param entryContext entry variance context reference
 * @param entryStatus entry variance status
 */
/**
 * Resolved DEBUG_ENTRY.
 * A debug entry entity.
 *
 * @param id STEP instance id
 * @param name entry name
 * @param entryType entry variance type
 * @param entryMessage entry variance debug message
 * @param entryDetails entry variance debug details
 * @param entryTimestamp entry variance timestamp
 * @param entryContext entry variance context reference
 * @param entryStatus entry variance status
 */
public final class StepDebugEntry implements StepEntity {
    private final int id;
    private final String name;
    private final String entryType;
    private final String entryMessage;
    private final List<String> entryDetails;
    private final StepEntity entryTimestamp;
    private final StepEntity entryContext;
    private final String entryStatus;

    public StepDebugEntry(int id, String name, String entryType, String entryMessage, List<String> entryDetails, StepEntity entryTimestamp, StepEntity entryContext, String entryStatus) {
        this.id = id;
        this.name = name;
        this.entryType = entryType;
        this.entryMessage = entryMessage;
        this.entryDetails = entryDetails == null ? null : java.util.List.copyOf(entryDetails);
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

    public String getEntryMessage() {
        return entryMessage;
    }

    public List<String> getEntryDetails() {
        return entryDetails;
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
        StepDebugEntry that = (StepDebugEntry) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(entryType, that.entryType) && Objects.equals(entryMessage, that.entryMessage) && Objects.equals(entryDetails, that.entryDetails) && Objects.equals(entryTimestamp, that.entryTimestamp) && Objects.equals(entryContext, that.entryContext) && Objects.equals(entryStatus, that.entryStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, entryType, entryMessage, entryDetails, entryTimestamp, entryContext, entryStatus);
    }

    @Override
    public String toString() {
        return "StepDebugEntry{" + "id=" + id + "name=" + name + "entryType=" + entryType + "entryMessage=" + entryMessage + "entryDetails=" + entryDetails + "entryTimestamp=" + entryTimestamp + "entryContext=" + entryContext + "entryStatus=" + entryStatus + "}";
    }
}