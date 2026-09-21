package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Resolved TIMEOUT_INSTANCE.
 * A timeout instance entity.
 *
 * @param id STEP instance id
 * @param name timeout instance name
 * @param timeoutDefinition timeout variance definition reference
 * @param timeoutState timeout variance state
 * @param timeoutStartTime timeout variance start time
 * @param timeoutTriggered timeout variance triggered flag
 * @param timeoutStatus timeout variance status
 */
public final class StepTimeoutInstance extends AbstractStepEntity {
    private final StepEntity timeoutDefinition;
    private final String timeoutState;
    private final StepEntity timeoutStartTime;
    private final boolean timeoutTriggered;
    private final String timeoutStatus;

    public StepTimeoutInstance(int id, String name, StepEntity timeoutDefinition, String timeoutState, StepEntity timeoutStartTime, boolean timeoutTriggered, String timeoutStatus) {
        super(id, name);
        this.timeoutDefinition = timeoutDefinition;
        this.timeoutState = timeoutState;
        this.timeoutStartTime = timeoutStartTime;
        this.timeoutTriggered = timeoutTriggered;
        this.timeoutStatus = timeoutStatus;
    }

    public StepEntity getTimeoutDefinition() {
        return timeoutDefinition;
    }

    public String getTimeoutState() {
        return timeoutState;
    }

    public StepEntity getTimeoutStartTime() {
        return timeoutStartTime;
    }

    public boolean isTimeoutTriggered() {
        return timeoutTriggered;
    }

    public String getTimeoutStatus() {
        return timeoutStatus;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("timeoutDefinition", timeoutDefinition);
        state.put("timeoutState", timeoutState);
        state.put("timeoutStartTime", timeoutStartTime);
        state.put("timeoutTriggered", timeoutTriggered);
        state.put("timeoutStatus", timeoutStatus);
        return state;
    }
}
