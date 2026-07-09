package com.minicad.step.model.action;

import com.minicad.step.model.core.base.StepEntity;
import java.util.List;
import java.util.Objects;

/**
 * Resolved TRIGGER_INSTANCE.
 * A trigger instance entity.
 *
 * @param id STEP instance id
 * @param name trigger instance name
 * @param triggerDefinition trigger variance definition reference
 * @param triggerState trigger variance state
 * @param triggerFireCount trigger variance fire count
 * @param triggerLastFire trigger variance last fire time
 * @param triggerStatus trigger variance status
 */
/**
 * Resolved TRIGGER_INSTANCE.
 * A trigger instance entity.
 *
 * @param id STEP instance id
 * @param name trigger instance name
 * @param triggerDefinition trigger variance definition reference
 * @param triggerState trigger variance state
 * @param triggerFireCount trigger variance fire count
 * @param triggerLastFire trigger variance last fire time
 * @param triggerStatus trigger variance status
 */
public final class StepTriggerInstance implements StepEntity {
    private final int id;
    private final String name;
    private final StepEntity triggerDefinition;
    private final String triggerState;
    private final int triggerFireCount;
    private final StepEntity triggerLastFire;
    private final String triggerStatus;

    public StepTriggerInstance(int id, String name, StepEntity triggerDefinition, String triggerState, int triggerFireCount, StepEntity triggerLastFire, String triggerStatus) {
        this.id = id;
        this.name = name;
        this.triggerDefinition = triggerDefinition;
        this.triggerState = triggerState;
        this.triggerFireCount = triggerFireCount;
        this.triggerLastFire = triggerLastFire;
        this.triggerStatus = triggerStatus;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public StepEntity getTriggerDefinition() {
        return triggerDefinition;
    }

    public String getTriggerState() {
        return triggerState;
    }

    public int getTriggerFireCount() {
        return triggerFireCount;
    }

    public StepEntity getTriggerLastFire() {
        return triggerLastFire;
    }

    public String getTriggerStatus() {
        return triggerStatus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepTriggerInstance that = (StepTriggerInstance) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(triggerDefinition, that.triggerDefinition) && Objects.equals(triggerState, that.triggerState) && triggerFireCount == that.triggerFireCount && Objects.equals(triggerLastFire, that.triggerLastFire) && Objects.equals(triggerStatus, that.triggerStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, triggerDefinition, triggerState, triggerFireCount, triggerLastFire, triggerStatus);
    }

    @Override
    public String toString() {
        return "StepTriggerInstance{" + "id=" + id + "name=" + name + "triggerDefinition=" + triggerDefinition + "triggerState=" + triggerState + "triggerFireCount=" + triggerFireCount + "triggerLastFire=" + triggerLastFire + "triggerStatus=" + triggerStatus + "}";
    }
}