package com.minicad.step.model;

import com.minicad.step.model.StepEntity;
import java.util.List;
import java.util.Objects;

/**
 * Resolved LOCK_ENTRY.
 * A lock entry entity.
 *
 * @param id STEP instance id
 * @param name entry name
 * @param entryType entry variance type
 * @param entryTarget entry variance target reference
 * @param entryHolder entry variance holder reference
 * @param entryState entry variance state
 * @param entryTimestamp entry variance timestamp
 * @param entryStatus entry variance status
 */
/**
 * Resolved LOCK_ENTRY.
 * A lock entry entity.
 *
 * @param id STEP instance id
 * @param name entry name
 * @param entryType entry variance type
 * @param entryTarget entry variance target reference
 * @param entryHolder entry variance holder reference
 * @param entryState entry variance state
 * @param entryTimestamp entry variance timestamp
 * @param entryStatus entry variance status
 */
public final class StepLockEntry implements StepEntity {
    private final int id;
    private final String name;
    private final String entryType;
    private final StepEntity entryTarget;
    private final StepEntity entryHolder;
    private final String entryState;
    private final StepEntity entryTimestamp;
    private final String entryStatus;

    public StepLockEntry(int id, String name, String entryType, StepEntity entryTarget, StepEntity entryHolder, String entryState, StepEntity entryTimestamp, String entryStatus) {
        this.id = id;
        this.name = name;
        this.entryType = entryType;
        this.entryTarget = entryTarget;
        this.entryHolder = entryHolder;
        this.entryState = entryState;
        this.entryTimestamp = entryTimestamp;
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

    public StepEntity getEntryTarget() {
        return entryTarget;
    }

    public StepEntity getEntryHolder() {
        return entryHolder;
    }

    public String getEntryState() {
        return entryState;
    }

    public StepEntity getEntryTimestamp() {
        return entryTimestamp;
    }

    public String getEntryStatus() {
        return entryStatus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepLockEntry that = (StepLockEntry) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(entryType, that.entryType) && Objects.equals(entryTarget, that.entryTarget) && Objects.equals(entryHolder, that.entryHolder) && Objects.equals(entryState, that.entryState) && Objects.equals(entryTimestamp, that.entryTimestamp) && Objects.equals(entryStatus, that.entryStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, entryType, entryTarget, entryHolder, entryState, entryTimestamp, entryStatus);
    }

    @Override
    public String toString() {
        return "StepLockEntry{" + "id=" + id + "name=" + name + "entryType=" + entryType + "entryTarget=" + entryTarget + "entryHolder=" + entryHolder + "entryState=" + entryState + "entryTimestamp=" + entryTimestamp + "entryStatus=" + entryStatus + "}";
    }
}