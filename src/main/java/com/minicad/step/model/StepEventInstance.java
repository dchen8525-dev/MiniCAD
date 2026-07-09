package com.minicad.step.model;

import com.minicad.step.model.StepEntity;
import java.util.List;
import java.util.Objects;

/**
 * Resolved EVENT_INSTANCE.
 * An event instance entity.
 *
 * @param id STEP instance id
 * @param name event instance name
 * @param eventDefinition event variance definition reference
 * @param eventState event variance state
 * @param eventTriggerTime event variance trigger time
 * @param eventExecutedActions event variance executed actions
 * @param eventStatus event variance status
 */
/**
 * Resolved EVENT_INSTANCE.
 * An event instance entity.
 *
 * @param id STEP instance id
 * @param name event instance name
 * @param eventDefinition event variance definition reference
 * @param eventState event variance state
 * @param eventTriggerTime event variance trigger time
 * @param eventExecutedActions event variance executed actions
 * @param eventStatus event variance status
 */
public final class StepEventInstance implements StepEntity {
    private final int id;
    private final String name;
    private final StepEntity eventDefinition;
    private final String eventState;
    private final StepEntity eventTriggerTime;
    private final List<String> eventExecutedActions;
    private final String eventStatus;

    public StepEventInstance(int id, String name, StepEntity eventDefinition, String eventState, StepEntity eventTriggerTime, List<String> eventExecutedActions, String eventStatus) {
        this.id = id;
        this.name = name;
        this.eventDefinition = eventDefinition;
        this.eventState = eventState;
        this.eventTriggerTime = eventTriggerTime;
        this.eventExecutedActions = eventExecutedActions == null ? null : java.util.List.copyOf(eventExecutedActions);
        this.eventStatus = eventStatus;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public StepEntity getEventDefinition() {
        return eventDefinition;
    }

    public String getEventState() {
        return eventState;
    }

    public StepEntity getEventTriggerTime() {
        return eventTriggerTime;
    }

    public List<String> getEventExecutedActions() {
        return eventExecutedActions;
    }

    public String getEventStatus() {
        return eventStatus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepEventInstance that = (StepEventInstance) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(eventDefinition, that.eventDefinition) && Objects.equals(eventState, that.eventState) && Objects.equals(eventTriggerTime, that.eventTriggerTime) && Objects.equals(eventExecutedActions, that.eventExecutedActions) && Objects.equals(eventStatus, that.eventStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, eventDefinition, eventState, eventTriggerTime, eventExecutedActions, eventStatus);
    }

    @Override
    public String toString() {
        return "StepEventInstance{" + "id=" + id + "name=" + name + "eventDefinition=" + eventDefinition + "eventState=" + eventState + "eventTriggerTime=" + eventTriggerTime + "eventExecutedActions=" + eventExecutedActions + "eventStatus=" + eventStatus + "}";
    }
}