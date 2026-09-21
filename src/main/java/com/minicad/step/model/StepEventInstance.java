package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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
public final class StepEventInstance extends AbstractStepEntity {
    private final StepEntity eventDefinition;
    private final String eventState;
    private final StepEntity eventTriggerTime;
    private final List<String> eventExecutedActions;
    private final String eventStatus;

    public StepEventInstance(int id, String name, StepEntity eventDefinition, String eventState, StepEntity eventTriggerTime, List<String> eventExecutedActions, String eventStatus) {
        super(id, name);
        this.eventDefinition = eventDefinition;
        this.eventState = eventState;
        this.eventTriggerTime = eventTriggerTime;
        this.eventExecutedActions = eventExecutedActions == null ? null : java.util.List.copyOf(eventExecutedActions);
        this.eventStatus = eventStatus;
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
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("eventDefinition", eventDefinition);
        state.put("eventState", eventState);
        state.put("eventTriggerTime", eventTriggerTime);
        state.put("eventExecutedActions", eventExecutedActions);
        state.put("eventStatus", eventStatus);
        return state;
    }
}
