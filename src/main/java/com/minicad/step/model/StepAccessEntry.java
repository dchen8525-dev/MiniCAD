package com.minicad.step.model;

import com.minicad.step.model.core.base.StepEntity;
import java.util.List;
import java.util.Objects;

/**
 * Resolved ACCESS_ENTRY.
 * An access entry entity.
 *
 * @param id STEP instance id
 * @param name entry name
 * @param entryType entry variance type
 * @param entryAction entry variance action (read/write/delete)
 * @param entryTarget entry variance target reference
 * @param entryActor entry variance actor reference
 * @param entryTimestamp entry variance timestamp
 * @param entryResult entry variance result
 * @param entryStatus entry variance status
 */
/**
 * Resolved ACCESS_ENTRY.
 * An access entry entity.
 *
 * @param id STEP instance id
 * @param name entry name
 * @param entryType entry variance type
 * @param entryAction entry variance action (read/write/delete)
 * @param entryTarget entry variance target reference
 * @param entryActor entry variance actor reference
 * @param entryTimestamp entry variance timestamp
 * @param entryResult entry variance result
 * @param entryStatus entry variance status
 */
public final class StepAccessEntry implements StepEntity {
    private final int id;
    private final String name;
    private final String entryType;
    private final String entryAction;
    private final StepEntity entryTarget;
    private final StepEntity entryActor;
    private final StepEntity entryTimestamp;
    private final String entryResult;
    private final String entryStatus;

    public StepAccessEntry(int id, String name, String entryType, String entryAction, StepEntity entryTarget, StepEntity entryActor, StepEntity entryTimestamp, String entryResult, String entryStatus) {
        this.id = id;
        this.name = name;
        this.entryType = entryType;
        this.entryAction = entryAction;
        this.entryTarget = entryTarget;
        this.entryActor = entryActor;
        this.entryTimestamp = entryTimestamp;
        this.entryResult = entryResult;
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

    public String getEntryAction() {
        return entryAction;
    }

    public StepEntity getEntryTarget() {
        return entryTarget;
    }

    public StepEntity getEntryActor() {
        return entryActor;
    }

    public StepEntity getEntryTimestamp() {
        return entryTimestamp;
    }

    public String getEntryResult() {
        return entryResult;
    }

    public String getEntryStatus() {
        return entryStatus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepAccessEntry that = (StepAccessEntry) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(entryType, that.entryType) && Objects.equals(entryAction, that.entryAction) && Objects.equals(entryTarget, that.entryTarget) && Objects.equals(entryActor, that.entryActor) && Objects.equals(entryTimestamp, that.entryTimestamp) && Objects.equals(entryResult, that.entryResult) && Objects.equals(entryStatus, that.entryStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, entryType, entryAction, entryTarget, entryActor, entryTimestamp, entryResult, entryStatus);
    }

    @Override
    public String toString() {
        return "StepAccessEntry{" + "id=" + id + "name=" + name + "entryType=" + entryType + "entryAction=" + entryAction + "entryTarget=" + entryTarget + "entryActor=" + entryActor + "entryTimestamp=" + entryTimestamp + "entryResult=" + entryResult + "entryStatus=" + entryStatus + "}";
    }
}