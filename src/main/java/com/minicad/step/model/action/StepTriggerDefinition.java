package com.minicad.step.model.action;

import com.minicad.step.model.base.StepEntity;
import java.util.List;

/**
 * Resolved TRIGGER_DEFINITION.
 * A trigger definition entity.
 *
 * @param id STEP instance id
 * @param name trigger name
 * @param triggerType trigger variance type
 * @param triggerCondition trigger variance condition
 * @param triggerAction trigger variance action reference
 * @param triggerSchedule trigger variance schedule
 * @param triggerStatus trigger variance status
 */
public record StepTriggerDefinition(
    int id,
    String name,
    String triggerType,
    String triggerCondition,
    StepEntity triggerAction,
    String triggerSchedule,
    String triggerStatus) implements StepEntity {
}