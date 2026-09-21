package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Resolved DEBUG_ENTRY.
 * A debug entry entity.
 *
 * @param id STEP instance id
 * @param name entry name
 * @param entryType entry variance type
 * @param entryMessage entry variance debug message
 * @param entryDetails entry variance debug details
 * @param entryTimestamp entry variance timestamp
 * @param entryContext entry variance context reference
 * @param entryStatus entry variance status
 */
public final class StepDebugEntry extends AbstractStepEntity {
    private final String entryType;
    private final String entryMessage;
    private final List<String> entryDetails;
    private final StepEntity entryTimestamp;
    private final StepEntity entryContext;
    private final String entryStatus;

    public StepDebugEntry(int id, String name, String entryType, String entryMessage, List<String> entryDetails, StepEntity entryTimestamp, StepEntity entryContext, String entryStatus) {
        super(id, name);
        this.entryType = entryType;
        this.entryMessage = entryMessage;
        this.entryDetails = entryDetails == null ? null : java.util.List.copyOf(entryDetails);
        this.entryTimestamp = entryTimestamp;
        this.entryContext = entryContext;
        this.entryStatus = entryStatus;
    }

    public String getEntryType() {
        return entryType;
    }

    public String getEntryMessage() {
        return entryMessage;
    }

    public List<String> getEntryDetails() {
        return entryDetails;
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
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("entryType", entryType);
        state.put("entryMessage", entryMessage);
        state.put("entryDetails", entryDetails);
        state.put("entryTimestamp", entryTimestamp);
        state.put("entryContext", entryContext);
        state.put("entryStatus", entryStatus);
        return state;
    }
}
