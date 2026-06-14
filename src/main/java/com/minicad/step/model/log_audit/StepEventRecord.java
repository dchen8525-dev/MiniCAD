package com.minicad.step.model.log_audit;

import com.minicad.step.model.base.StepEntity;
import java.util.List;
import java.util.Objects;

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
public final class StepEventRecord implements StepEntity {
    private final int id;
    private final String name;
    private final String eventType;
    private final StepEntity eventSource;
    private final StepEntity eventTime;
    private final List<String> eventDetails;
    private final boolean eventProcessed;
    private final String eventStatus;

    public StepEventRecord(int id, String name, String eventType, StepEntity eventSource, StepEntity eventTime, List<String> eventDetails, boolean eventProcessed, String eventStatus) {
        this.id = id;
        this.name = name;
        this.eventType = eventType;
        this.eventSource = eventSource;
        this.eventTime = eventTime;
        this.eventDetails = eventDetails == null ? null : java.util.List.copyOf(eventDetails);
        this.eventProcessed = eventProcessed;
        this.eventStatus = eventStatus;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepEventRecord that = (StepEventRecord) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(eventType, that.eventType) && Objects.equals(eventSource, that.eventSource) && Objects.equals(eventTime, that.eventTime) && Objects.equals(eventDetails, that.eventDetails) && eventProcessed == that.eventProcessed && Objects.equals(eventStatus, that.eventStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, eventType, eventSource, eventTime, eventDetails, eventProcessed, eventStatus);
    }

    @Override
    public String toString() {
        return "StepEventRecord{" + "id=" + id + "name=" + name + "eventType=" + eventType + "eventSource=" + eventSource + "eventTime=" + eventTime + "eventDetails=" + eventDetails + "eventProcessed=" + eventProcessed + "eventStatus=" + eventStatus + "}";
    }
}