package com.minicad.step.model;

import java.util.List;
import java.util.Map;

/**
 * Resolved INFO_ENTRY.
 * An info entry entity.
 *
 * @param id STEP instance id
 * @param name entry name
 * @param entryType entry variance type
 * @param entryMessage entry variance info message
 * @param entryTimestamp entry variance timestamp
 * @param entryContext entry variance context reference
 * @param entryStatus entry variance status
 */
public final class StepInfoEntry extends AbstractStepEntity {
    private final String entryType;
    private final String entryMessage;
    private final StepEntity entryTimestamp;
    private final StepEntity entryContext;
    private final String entryStatus;

    public StepInfoEntry(int id, String name, String entryType, String entryMessage, StepEntity entryTimestamp, StepEntity entryContext, String entryStatus) {
        super(id, name);
        this.entryType = entryType;
        this.entryMessage = entryMessage;
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
        return componentsOf(
                "id", getId(), "name", getName(), "entryType", entryType, "entryMessage", entryMessage,
                "entryTimestamp", entryTimestamp, "entryContext", entryContext, "entryStatus", entryStatus);
    }
}
