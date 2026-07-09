package com.minicad.step.model;

import com.minicad.step.model.core.base.StepEntity;
import java.util.List;
import java.util.Objects;

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
public final class StepTriggerDefinition implements StepEntity {
    private final int id;
    private final String name;
    private final String triggerType;
    private final String triggerCondition;
    private final StepEntity triggerAction;
    private final String triggerSchedule;
    private final String triggerStatus;

    public StepTriggerDefinition(int id, String name, String triggerType, String triggerCondition, StepEntity triggerAction, String triggerSchedule, String triggerStatus) {
        this.id = id;
        this.name = name;
        this.triggerType = triggerType;
        this.triggerCondition = triggerCondition;
        this.triggerAction = triggerAction;
        this.triggerSchedule = triggerSchedule;
        this.triggerStatus = triggerStatus;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepTriggerDefinition that = (StepTriggerDefinition) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(triggerType, that.triggerType) && Objects.equals(triggerCondition, that.triggerCondition) && Objects.equals(triggerAction, that.triggerAction) && Objects.equals(triggerSchedule, that.triggerSchedule) && Objects.equals(triggerStatus, that.triggerStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, triggerType, triggerCondition, triggerAction, triggerSchedule, triggerStatus);
    }

    @Override
    public String toString() {
        return "StepTriggerDefinition{" + "id=" + id + "name=" + name + "triggerType=" + triggerType + "triggerCondition=" + triggerCondition + "triggerAction=" + triggerAction + "triggerSchedule=" + triggerSchedule + "triggerStatus=" + triggerStatus + "}";
    }
}