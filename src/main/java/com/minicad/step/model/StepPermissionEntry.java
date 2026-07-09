package com.minicad.step.model.management.security;

import com.minicad.step.model.core.base.StepEntity;
import java.util.List;
import java.util.Objects;

/**
 * Resolved PERMISSION_ENTRY.
 * A permission entry entity.
 *
 * @param id STEP instance id
 * @param name entry name
 * @param entryType entry variance type
 * @param entryPermission entry variance permission
 * @param entryTarget entry variance target reference
 * @param entryHolder entry variance holder reference
 * @param entryGranted entry variance granted flag
 * @param entryTimestamp entry variance timestamp
 * @param entryStatus entry variance status
 */
/**
 * Resolved PERMISSION_ENTRY.
 * A permission entry entity.
 *
 * @param id STEP instance id
 * @param name entry name
 * @param entryType entry variance type
 * @param entryPermission entry variance permission
 * @param entryTarget entry variance target reference
 * @param entryHolder entry variance holder reference
 * @param entryGranted entry variance granted flag
 * @param entryTimestamp entry variance timestamp
 * @param entryStatus entry variance status
 */
public final class StepPermissionEntry implements StepEntity {
    private final int id;
    private final String name;
    private final String entryType;
    private final String entryPermission;
    private final StepEntity entryTarget;
    private final StepEntity entryHolder;
    private final boolean entryGranted;
    private final StepEntity entryTimestamp;
    private final String entryStatus;

    public StepPermissionEntry(int id, String name, String entryType, String entryPermission, StepEntity entryTarget, StepEntity entryHolder, boolean entryGranted, StepEntity entryTimestamp, String entryStatus) {
        this.id = id;
        this.name = name;
        this.entryType = entryType;
        this.entryPermission = entryPermission;
        this.entryTarget = entryTarget;
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

    public String getEntryPermission() {
        return entryPermission;
    }

    public StepEntity getEntryTarget() {
        return entryTarget;
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
        StepPermissionEntry that = (StepPermissionEntry) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(entryType, that.entryType) && Objects.equals(entryPermission, that.entryPermission) && Objects.equals(entryTarget, that.entryTarget) && Objects.equals(entryHolder, that.entryHolder) && entryGranted == that.entryGranted && Objects.equals(entryTimestamp, that.entryTimestamp) && Objects.equals(entryStatus, that.entryStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, entryType, entryPermission, entryTarget, entryHolder, entryGranted, entryTimestamp, entryStatus);
    }

    @Override
    public String toString() {
        return "StepPermissionEntry{" + "id=" + id + "name=" + name + "entryType=" + entryType + "entryPermission=" + entryPermission + "entryTarget=" + entryTarget + "entryHolder=" + entryHolder + "entryGranted=" + entryGranted + "entryTimestamp=" + entryTimestamp + "entryStatus=" + entryStatus + "}";
    }
}