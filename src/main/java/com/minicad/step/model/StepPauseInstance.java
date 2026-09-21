package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Resolved PAUSE_INSTANCE.
 * A pause instance entity.
 *
 * @param id STEP instance id
 * @param name pause instance name
 * @param pauseDefinition pause variance definition reference
 * @param pauseState pause variance state
 * @param pauseStartTime pause variance start time
 * @param pauseDuration pause variance current duration
 * @param pauseStatus pause variance status
 */
public final class StepPauseInstance extends AbstractStepEntity {
    private final StepEntity pauseDefinition;
    private final String pauseState;
    private final StepEntity pauseStartTime;
    private final int pauseDuration;
    private final String pauseStatus;

    public StepPauseInstance(int id, String name, StepEntity pauseDefinition, String pauseState, StepEntity pauseStartTime, int pauseDuration, String pauseStatus) {
        super(id, name);
        this.pauseDefinition = pauseDefinition;
        this.pauseState = pauseState;
        this.pauseStartTime = pauseStartTime;
        this.pauseDuration = pauseDuration;
        this.pauseStatus = pauseStatus;
    }

    public StepEntity getPauseDefinition() {
        return pauseDefinition;
    }

    public String getPauseState() {
        return pauseState;
    }

    public StepEntity getPauseStartTime() {
        return pauseStartTime;
    }

    public int getPauseDuration() {
        return pauseDuration;
    }

    public String getPauseStatus() {
        return pauseStatus;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("pauseDefinition", pauseDefinition);
        state.put("pauseState", pauseState);
        state.put("pauseStartTime", pauseStartTime);
        state.put("pauseDuration", pauseDuration);
        state.put("pauseStatus", pauseStatus);
        return state;
    }
}
