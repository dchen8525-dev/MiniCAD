package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Resolved WAIT_INSTANCE.
 * A wait instance entity.
 *
 * @param id STEP instance id
 * @param name wait instance name
 * @param waitDefinition wait variance definition reference
 * @param waitState wait variance state
 * @param waitStartTime wait variance start time
 * @param waitConditionMet wait variance condition met flag
 * @param waitStatus wait variance status
 */
public final class StepWaitInstance extends AbstractStepEntity {
    private final StepEntity waitDefinition;
    private final String waitState;
    private final StepEntity waitStartTime;
    private final boolean waitConditionMet;
    private final String waitStatus;

    public StepWaitInstance(int id, String name, StepEntity waitDefinition, String waitState, StepEntity waitStartTime, boolean waitConditionMet, String waitStatus) {
        super(id, name);
        this.waitDefinition = waitDefinition;
        this.waitState = waitState;
        this.waitStartTime = waitStartTime;
        this.waitConditionMet = waitConditionMet;
        this.waitStatus = waitStatus;
    }

    public StepEntity getWaitDefinition() {
        return waitDefinition;
    }

    public String getWaitState() {
        return waitState;
    }

    public StepEntity getWaitStartTime() {
        return waitStartTime;
    }

    public boolean isWaitConditionMet() {
        return waitConditionMet;
    }

    public String getWaitStatus() {
        return waitStatus;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("waitDefinition", waitDefinition);
        state.put("waitState", waitState);
        state.put("waitStartTime", waitStartTime);
        state.put("waitConditionMet", waitConditionMet);
        state.put("waitStatus", waitStatus);
        return state;
    }
}
