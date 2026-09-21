package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Resolved TIMER_INSTANCE.
 * A timer instance entity.
 *
 * @param id STEP instance id
 * @param name timer instance name
 * @param timerDefinition timer variance definition reference
 * @param timerState timer variance state
 * @param timerStartTime timer variance start time
 * @param timerRemaining timer variance remaining time
 * @param timerStatus timer variance status
 */
public final class StepTimerInstance extends AbstractStepEntity {
    private final StepEntity timerDefinition;
    private final String timerState;
    private final StepEntity timerStartTime;
    private final int timerRemaining;
    private final String timerStatus;

    public StepTimerInstance(int id, String name, StepEntity timerDefinition, String timerState, StepEntity timerStartTime, int timerRemaining, String timerStatus) {
        super(id, name);
        this.timerDefinition = timerDefinition;
        this.timerState = timerState;
        this.timerStartTime = timerStartTime;
        this.timerRemaining = timerRemaining;
        this.timerStatus = timerStatus;
    }

    public StepEntity getTimerDefinition() {
        return timerDefinition;
    }

    public String getTimerState() {
        return timerState;
    }

    public StepEntity getTimerStartTime() {
        return timerStartTime;
    }

    public int getTimerRemaining() {
        return timerRemaining;
    }

    public String getTimerStatus() {
        return timerStatus;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("timerDefinition", timerDefinition);
        state.put("timerState", timerState);
        state.put("timerStartTime", timerStartTime);
        state.put("timerRemaining", timerRemaining);
        state.put("timerStatus", timerStatus);
        return state;
    }
}
