package com.minicad.step.model;

import java.util.List;
import java.util.Map;

/**
 * Resolved WARNING_ENTRY.
 * A warning entry entity.
 *
 * @param id STEP instance id
 * @param name entry name
 * @param entryType entry variance type
 * @param entryCode entry variance warning code
 * @param entryMessage entry variance warning message
 * @param entrySeverity entry variance severity level
 * @param entryTimestamp entry variance timestamp
 * @param entryContext entry variance context reference
 * @param entryStatus entry variance status
 */
public final class StepWarningEntry extends AbstractStepEntity {
    private final String entryType;
    private final String entryCode;
    private final String entryMessage;
    private final int entrySeverity;
    private final StepEntity entryTimestamp;
    private final StepEntity entryContext;
    private final String entryStatus;

    public StepWarningEntry(int id, String name, String entryType, String entryCode, String entryMessage, int entrySeverity, StepEntity entryTimestamp, StepEntity entryContext, String entryStatus) {
        super(id, name);
        this.entryType = entryType;
        this.entryCode = entryCode;
        this.entryMessage = entryMessage;
        this.entrySeverity = entrySeverity;
        this.entryTimestamp = entryTimestamp;
        this.entryContext = entryContext;
        this.entryStatus = entryStatus;
    }

    public String getEntryType() {
        return entryType;
    }

    public String getEntryCode() {
        return entryCode;
    }

    public String getEntryMessage() {
        return entryMessage;
    }

    public int getEntrySeverity() {
        return entrySeverity;
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
                "id", getId(), "name", getName(), "entryType", entryType, "entryCode", entryCode,
                "entryMessage", entryMessage, "entrySeverity", entrySeverity,
                "entryTimestamp", entryTimestamp, "entryContext", entryContext, "entryStatus", entryStatus);
    }
}
