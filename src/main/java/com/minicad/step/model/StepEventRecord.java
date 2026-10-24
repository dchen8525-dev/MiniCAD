package com.minicad.step.model;

import java.util.List;
import java.util.Map;

/**
 * Resolved EVENT_RECORD.
 * An event record entity.
 *
 * @param id STEP instance id
 * @param name event name
 * @param eventType event variance type
 * @param eventSource event variance source reference
 * @param eventTime event variance occurrence time
 * @param eventDetails event variance details
 * @param eventProcessed event variance processed flag
 * @param eventStatus event variance status
 */
public final class StepEventRecord extends AbstractStepEntity {
    private final String eventType;
    private final StepEntity eventSource;
    private final StepEntity eventTime;
    private final List<String> eventDetails;
    private final boolean eventProcessed;
    private final String eventStatus;

    public StepEventRecord(int id, String name, String eventType, StepEntity eventSource, StepEntity eventTime, List<String> eventDetails, boolean eventProcessed, String eventStatus) {
        super(id, name);
        this.eventType = eventType;
        this.eventSource = eventSource;
        this.eventTime = eventTime;
        this.eventDetails = eventDetails == null ? null : java.util.List.copyOf(eventDetails);
        this.eventProcessed = eventProcessed;
        this.eventStatus = eventStatus;
    }

    public String getEventType() {
        return eventType;
    }

    public StepEntity getEventSource() {
        return eventSource;
    }

    public StepEntity getEventTime() {
        return eventTime;
    }

    public List<String> getEventDetails() {
        return eventDetails;
    }

    public boolean isEventProcessed() {
        return eventProcessed;
    }

    public String getEventStatus() {
        return eventStatus;
    }

    @Override
    protected Map<String, Object> components() {
        return componentsOf(
                "id", getId(), "name", getName(), "eventType", eventType, "eventSource", eventSource,
                "eventTime", eventTime, "eventDetails", eventDetails, "eventProcessed", eventProcessed,
                "eventStatus", eventStatus);
    }
}
