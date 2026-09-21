package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Resolved TRACE_ENTRY.
 * A trace entry entity.
 *
 * @param id STEP instance id
 * @param name entry name
 * @param entryType entry variance type
 * @param entryOperation entry variance operation name
 * @param entryDetails entry variance trace details
 * @param entryTimestamp entry variance timestamp
 * @param entryDuration entry variance duration
 * @param entryStatus entry variance status
 */
public final class StepTraceEntry extends AbstractStepEntity {
    private final String entryType;
    private final String entryOperation;
    private final List<String> entryDetails;
    private final StepEntity entryTimestamp;
    private final long entryDuration;
    private final String entryStatus;

    public StepTraceEntry(int id, String name, String entryType, String entryOperation, List<String> entryDetails, StepEntity entryTimestamp, long entryDuration, String entryStatus) {
        super(id, name);
        this.entryType = entryType;
        this.entryOperation = entryOperation;
        this.entryDetails = entryDetails == null ? null : java.util.List.copyOf(entryDetails);
        this.entryTimestamp = entryTimestamp;
        this.entryDuration = entryDuration;
        this.entryStatus = entryStatus;
    }

    public String getEntryType() {
        return entryType;
    }

    public String getEntryOperation() {
        return entryOperation;
    }

    public List<String> getEntryDetails() {
        return entryDetails;
    }

    public StepEntity getEntryTimestamp() {
        return entryTimestamp;
    }

    public long getEntryDuration() {
        return entryDuration;
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
        state.put("entryOperation", entryOperation);
        state.put("entryDetails", entryDetails);
        state.put("entryTimestamp", entryTimestamp);
        state.put("entryDuration", entryDuration);
        state.put("entryStatus", entryStatus);
        return state;
    }
}
