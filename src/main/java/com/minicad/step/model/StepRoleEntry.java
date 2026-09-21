package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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
public final class StepRoleEntry extends AbstractStepEntity {
    private final String entryType;
    private final StepEntity entryRole;
    private final StepEntity entryHolder;
    private final boolean entryGranted;
    private final StepEntity entryTimestamp;
    private final String entryStatus;

    public StepRoleEntry(int id, String name, String entryType, StepEntity entryRole, StepEntity entryHolder, boolean entryGranted, StepEntity entryTimestamp, String entryStatus) {
        super(id, name);
        this.entryType = entryType;
        this.entryRole = entryRole;
        this.entryHolder = entryHolder;
        this.entryGranted = entryGranted;
        this.entryTimestamp = entryTimestamp;
        this.entryStatus = entryStatus;
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
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("entryType", entryType);
        state.put("entryRole", entryRole);
        state.put("entryHolder", entryHolder);
        state.put("entryGranted", entryGranted);
        state.put("entryTimestamp", entryTimestamp);
        state.put("entryStatus", entryStatus);
        return state;
    }
}
