package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Resolved DELAY_INSTANCE.
 * A delay instance entity.
 *
 * @param id STEP instance id
 * @param name delay instance name
 * @param delayDefinition delay variance definition reference
 * @param delayState delay variance state
 * @param delayStartTime delay variance start time
 * @param delayEndTime delay variance expected end time
 * @param delayRemaining delay variance remaining time
 * @param delayStatus delay variance status
 */
public final class StepDelayInstance extends AbstractStepEntity {
    private final StepEntity delayDefinition;
    private final String delayState;
    private final StepEntity delayStartTime;
    private final StepEntity delayEndTime;
    private final int delayRemaining;
    private final String delayStatus;

    public StepDelayInstance(int id, String name, StepEntity delayDefinition, String delayState, StepEntity delayStartTime, StepEntity delayEndTime, int delayRemaining, String delayStatus) {
        super(id, name);
        this.delayDefinition = delayDefinition;
        this.delayState = delayState;
        this.delayStartTime = delayStartTime;
        this.delayEndTime = delayEndTime;
        this.delayRemaining = delayRemaining;
        this.delayStatus = delayStatus;
    }

    public StepEntity getDelayDefinition() {
        return delayDefinition;
    }

    public String getDelayState() {
        return delayState;
    }

    public StepEntity getDelayStartTime() {
        return delayStartTime;
    }

    public StepEntity getDelayEndTime() {
        return delayEndTime;
    }

    public int getDelayRemaining() {
        return delayRemaining;
    }

    public String getDelayStatus() {
        return delayStatus;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("delayDefinition", delayDefinition);
        state.put("delayState", delayState);
        state.put("delayStartTime", delayStartTime);
        state.put("delayEndTime", delayEndTime);
        state.put("delayRemaining", delayRemaining);
        state.put("delayStatus", delayStatus);
        return state;
    }
}
