package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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
public final class StepLockEntry extends AbstractStepEntity {
    private final String entryType;
    private final StepEntity entryTarget;
    private final StepEntity entryHolder;
    private final String entryState;
    private final StepEntity entryTimestamp;
    private final String entryStatus;

    public StepLockEntry(int id, String name, String entryType, StepEntity entryTarget, StepEntity entryHolder, String entryState, StepEntity entryTimestamp, String entryStatus) {
        super(id, name);
        this.entryType = entryType;
        this.entryTarget = entryTarget;
        this.entryHolder = entryHolder;
        this.entryState = entryState;
        this.entryTimestamp = entryTimestamp;
        this.entryStatus = entryStatus;
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
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("entryType", entryType);
        state.put("entryTarget", entryTarget);
        state.put("entryHolder", entryHolder);
        state.put("entryState", entryState);
        state.put("entryTimestamp", entryTimestamp);
        state.put("entryStatus", entryStatus);
        return state;
    }
}
