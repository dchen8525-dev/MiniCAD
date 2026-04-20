package com.minicad.step.model.action;

import com.minicad.step.model.base.StepEntity;
import java.util.List;

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
public record StepEventInstance(
    int id,
    String name,
    StepEntity eventDefinition,
    String eventState,
    StepEntity eventTriggerTime,
    List<String> eventExecutedActions,
    String eventStatus) implements StepEntity {
}