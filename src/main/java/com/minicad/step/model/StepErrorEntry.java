package com.minicad.step.model;

import com.minicad.step.model.StepEntity;
import java.util.List;
import java.util.Objects;

/**
 * Resolved ERROR_ENTRY.
 * An error entry entity.
 *
 * @param id STEP instance id
 * @param name entry name
 * @param entryType entry variance type
 * @param entryCode entry variance error code
 * @param entryMessage entry variance error message
 * @param entrySeverity entry variance severity level
 * @param entryTimestamp entry variance timestamp
 * @param entryContext entry variance context reference
 * @param entryStatus entry variance status
 */
/**
 * Resolved ERROR_ENTRY.
 * An error entry entity.
 *
 * @param id STEP instance id
 * @param name entry name
 * @param entryType entry variance type
 * @param entryCode entry variance error code
 * @param entryMessage entry variance error message
 * @param entrySeverity entry variance severity level
 * @param entryTimestamp entry variance timestamp
 * @param entryContext entry variance context reference
 * @param entryStatus entry variance status
 */
public final class StepErrorEntry implements StepEntity {
    private final int id;
    private final String name;
    private final String entryType;
    private final String entryCode;
    private final String entryMessage;
    private final int entrySeverity;
    private final StepEntity entryTimestamp;
    private final StepEntity entryContext;
    private final String entryStatus;

    public StepErrorEntry(int id, String name, String entryType, String entryCode, String entryMessage, int entrySeverity, StepEntity entryTimestamp, StepEntity entryContext, String entryStatus) {
        this.id = id;
        this.name = name;
        this.entryType = entryType;
        this.entryCode = entryCode;
        this.entryMessage = entryMessage;
        this.entrySeverity = entrySeverity;
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

    public String getEntryCode() {
        return entryCode;
    }

    public String getEntryMessage() {
        return entryMessage;
    }

    public int getEntrySeverity() {
        return entrySeverity;
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
        StepErrorEntry that = (StepErrorEntry) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(entryType, that.entryType) && Objects.equals(entryCode, that.entryCode) && Objects.equals(entryMessage, that.entryMessage) && entrySeverity == that.entrySeverity && Objects.equals(entryTimestamp, that.entryTimestamp) && Objects.equals(entryContext, that.entryContext) && Objects.equals(entryStatus, that.entryStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, entryType, entryCode, entryMessage, entrySeverity, entryTimestamp, entryContext, entryStatus);
    }

    @Override
    public String toString() {
        return "StepErrorEntry{" + "id=" + id + "name=" + name + "entryType=" + entryType + "entryCode=" + entryCode + "entryMessage=" + entryMessage + "entrySeverity=" + entrySeverity + "entryTimestamp=" + entryTimestamp + "entryContext=" + entryContext + "entryStatus=" + entryStatus + "}";
    }
}