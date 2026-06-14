package com.minicad.step.model.log_audit;

import com.minicad.step.model.base.StepEntity;
import java.util.List;
import java.util.Objects;

/**
 * Resolved ROLE_ENTRY.
 * A role entry entity.
 *
 * @param id STEP instance id
 * @param name entry name
 * @param entryType entry variance type
 * @param entryRole entry variance role reference
 * @param entryHolder entry variance holder reference
 * @param entryGranted entry variance granted flag
 * @param entryTimestamp entry variance timestamp
 * @param entryStatus entry variance status
 */
/**
 * Resolved ROLE_ENTRY.
 * A role entry entity.
 *
 * @param id STEP instance id
 * @param name entry name
 * @param entryType entry variance type
 * @param entryRole entry variance role reference
 * @param entryHolder entry variance holder reference
 * @param entryGranted entry variance granted flag
 * @param entryTimestamp entry variance timestamp
 * @param entryStatus entry variance status
 */
public final class StepRoleEntry implements StepEntity {
    private final int id;
    private final String name;
    private final String entryType;
    private final StepEntity entryRole;
    private final StepEntity entryHolder;
    private final boolean entryGranted;
    private final StepEntity entryTimestamp;
    private final String entryStatus;

    public StepRoleEntry(int id, String name, String entryType, StepEntity entryRole, StepEntity entryHolder, boolean entryGranted, StepEntity entryTimestamp, String entryStatus) {
        this.id = id;
        this.name = name;
        this.entryType = entryType;
        this.entryRole = entryRole;
        this.entryHolder = entryHolder;
        this.entryGranted = entryGranted;
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

    public StepEntity getEntryRole() {
        return entryRole;
    }

    public StepEntity getEntryHolder() {
        return entryHolder;
    }

    public boolean isEntryGranted() {
        return entryGranted;
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
        StepRoleEntry that = (StepRoleEntry) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(entryType, that.entryType) && Objects.equals(entryRole, that.entryRole) && Objects.equals(entryHolder, that.entryHolder) && entryGranted == that.entryGranted && Objects.equals(entryTimestamp, that.entryTimestamp) && Objects.equals(entryStatus, that.entryStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, entryType, entryRole, entryHolder, entryGranted, entryTimestamp, entryStatus);
    }

    @Override
    public String toString() {
        return "StepRoleEntry{" + "id=" + id + "name=" + name + "entryType=" + entryType + "entryRole=" + entryRole + "entryHolder=" + entryHolder + "entryGranted=" + entryGranted + "entryTimestamp=" + entryTimestamp + "entryStatus=" + entryStatus + "}";
    }
}