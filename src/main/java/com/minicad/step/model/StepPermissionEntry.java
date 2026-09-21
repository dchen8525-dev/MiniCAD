package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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
public final class StepPermissionEntry extends AbstractStepEntity {
    private final String entryType;
    private final String entryPermission;
    private final StepEntity entryTarget;
    private final StepEntity entryHolder;
    private final boolean entryGranted;
    private final StepEntity entryTimestamp;
    private final String entryStatus;

    public StepPermissionEntry(int id, String name, String entryType, String entryPermission, StepEntity entryTarget, StepEntity entryHolder, boolean entryGranted, StepEntity entryTimestamp, String entryStatus) {
        super(id, name);
        this.entryType = entryType;
        this.entryPermission = entryPermission;
        this.entryTarget = entryTarget;
        this.entryHolder = entryHolder;
        this.entryGranted = entryGranted;
        this.entryTimestamp = entryTimestamp;
        this.entryStatus = entryStatus;
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
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("entryType", entryType);
        state.put("entryPermission", entryPermission);
        state.put("entryTarget", entryTarget);
        state.put("entryHolder", entryHolder);
        state.put("entryGranted", entryGranted);
        state.put("entryTimestamp", entryTimestamp);
        state.put("entryStatus", entryStatus);
        return state;
    }
}
