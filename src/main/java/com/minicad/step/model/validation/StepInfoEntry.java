package com.minicad.step.model.validation;

import com.minicad.step.model.base.StepEntity;
import java.util.List;
import java.util.Objects;

/**
 * Resolved INFO_ENTRY.
 * An info entry entity.
 *
 * @param id STEP instance id
 * @param name entry name
 * @param entryType entry variance type
 * @param entryMessage entry variance info message
 * @param entryTimestamp entry variance timestamp
 * @param entryContext entry variance context reference
 * @param entryStatus entry variance status
 */
/**
 * Resolved INFO_ENTRY.
 * An info entry entity.
 *
 * @param id STEP instance id
 * @param name entry name
 * @param entryType entry variance type
 * @param entryMessage entry variance info message
 * @param entryTimestamp entry variance timestamp
 * @param entryContext entry variance context reference
 * @param entryStatus entry variance status
 */
public final class StepInfoEntry implements StepEntity {
    private final int id;
    private final String name;
    private final String entryType;
    private final String entryMessage;
    private final StepEntity entryTimestamp;
    private final StepEntity entryContext;
    private final String entryStatus;

    public StepInfoEntry(int id, String name, String entryType, String entryMessage, StepEntity entryTimestamp, StepEntity entryContext, String entryStatus) {
        this.id = id;
        this.name = name;
        this.entryType = entryType;
        this.entryMessage = entryMessage;
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
        StepInfoEntry that = (StepInfoEntry) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(entryType, that.entryType) && Objects.equals(entryMessage, that.entryMessage) && Objects.equals(entryTimestamp, that.entryTimestamp) && Objects.equals(entryContext, that.entryContext) && Objects.equals(entryStatus, that.entryStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, entryType, entryMessage, entryTimestamp, entryContext, entryStatus);
    }

    @Override
    public String toString() {
        return "StepInfoEntry{" + "id=" + id + "name=" + name + "entryType=" + entryType + "entryMessage=" + entryMessage + "entryTimestamp=" + entryTimestamp + "entryContext=" + entryContext + "entryStatus=" + entryStatus + "}";
    }
}