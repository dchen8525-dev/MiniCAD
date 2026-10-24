package com.minicad.step.model;

import java.util.List;
import java.util.Map;

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
public final class StepTriggerDefinition extends AbstractStepEntity {
    private final String triggerType;
    private final String triggerCondition;
    private final StepEntity triggerAction;
    private final String triggerSchedule;
    private final String triggerStatus;

    public StepTriggerDefinition(int id, String name, String triggerType, String triggerCondition, StepEntity triggerAction, String triggerSchedule, String triggerStatus) {
        super(id, name);
        this.triggerType = triggerType;
        this.triggerCondition = triggerCondition;
        this.triggerAction = triggerAction;
        this.triggerSchedule = triggerSchedule;
        this.triggerStatus = triggerStatus;
    }

    public String getTriggerType() {
        return triggerType;
    }

    public String getTriggerCondition() {
        return triggerCondition;
    }

    public StepEntity getTriggerAction() {
        return triggerAction;
    }

    public String getTriggerSchedule() {
        return triggerSchedule;
    }

    public String getTriggerStatus() {
        return triggerStatus;
    }

    @Override
    protected Map<String, Object> components() {
        return componentsOf(
                "id", getId(), "name", getName(), "triggerType", triggerType,
                "triggerCondition", triggerCondition, "triggerAction", triggerAction,
                "triggerSchedule", triggerSchedule, "triggerStatus", triggerStatus);
    }
}
