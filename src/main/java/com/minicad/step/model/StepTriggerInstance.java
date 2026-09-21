package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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
public final class StepTriggerInstance extends AbstractStepEntity {
    private final StepEntity triggerDefinition;
    private final String triggerState;
    private final int triggerFireCount;
    private final StepEntity triggerLastFire;
    private final String triggerStatus;

    public StepTriggerInstance(int id, String name, StepEntity triggerDefinition, String triggerState, int triggerFireCount, StepEntity triggerLastFire, String triggerStatus) {
        super(id, name);
        this.triggerDefinition = triggerDefinition;
        this.triggerState = triggerState;
        this.triggerFireCount = triggerFireCount;
        this.triggerLastFire = triggerLastFire;
        this.triggerStatus = triggerStatus;
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
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("triggerDefinition", triggerDefinition);
        state.put("triggerState", triggerState);
        state.put("triggerFireCount", triggerFireCount);
        state.put("triggerLastFire", triggerLastFire);
        state.put("triggerStatus", triggerStatus);
        return state;
    }
}
