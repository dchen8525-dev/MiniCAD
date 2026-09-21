package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Resolved TIMER_DEFINITION.
 * A timer definition entity.
 *
 * @param id STEP instance id
 * @param name timer name
 * @param timerType timer variance type
 * @param timerDuration timer variance duration
 * @param timerAction timer variance action reference
 * @param timerRecurring timer variance recurring flag
 * @param timerStatus timer variance status
 */
public final class StepTimerDefinition extends AbstractStepEntity {
    private final String timerType;
    private final int timerDuration;
    private final StepEntity timerAction;
    private final boolean timerRecurring;
    private final String timerStatus;

    public StepTimerDefinition(int id, String name, String timerType, int timerDuration, StepEntity timerAction, boolean timerRecurring, String timerStatus) {
        super(id, name);
        this.timerType = timerType;
        this.timerDuration = timerDuration;
        this.timerAction = timerAction;
        this.timerRecurring = timerRecurring;
        this.timerStatus = timerStatus;
    }

    public String getTimerType() {
        return timerType;
    }

    public int getTimerDuration() {
        return timerDuration;
    }

    public StepEntity getTimerAction() {
        return timerAction;
    }

    public boolean isTimerRecurring() {
        return timerRecurring;
    }

    public String getTimerStatus() {
        return timerStatus;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("timerType", timerType);
        state.put("timerDuration", timerDuration);
        state.put("timerAction", timerAction);
        state.put("timerRecurring", timerRecurring);
        state.put("timerStatus", timerStatus);
        return state;
    }
}
