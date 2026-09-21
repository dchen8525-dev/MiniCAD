package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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
public final class StepAccessEntry extends AbstractStepEntity {
    private final String entryType;
    private final String entryAction;
    private final StepEntity entryTarget;
    private final StepEntity entryActor;
    private final StepEntity entryTimestamp;
    private final String entryResult;
    private final String entryStatus;

    public StepAccessEntry(int id, String name, String entryType, String entryAction, StepEntity entryTarget, StepEntity entryActor, StepEntity entryTimestamp, String entryResult, String entryStatus) {
        super(id, name);
        this.entryType = entryType;
        this.entryAction = entryAction;
        this.entryTarget = entryTarget;
        this.entryActor = entryActor;
        this.entryTimestamp = entryTimestamp;
        this.entryResult = entryResult;
        this.entryStatus = entryStatus;
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
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("entryType", entryType);
        state.put("entryAction", entryAction);
        state.put("entryTarget", entryTarget);
        state.put("entryActor", entryActor);
        state.put("entryTimestamp", entryTimestamp);
        state.put("entryResult", entryResult);
        state.put("entryStatus", entryStatus);
        return state;
    }
}
