package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Resolved RECOVERY_INSTANCE.
 * A recovery instance entity.
 *
 * @param id STEP instance id
 * @param name recovery instance name
 * @param recoveryDefinition recovery variance definition reference
 * @param recoveryState recovery variance state
 * @param recoveryStartTime recovery variance start time
 * @param recoveryEndTime recovery variance end time
 * @param recoveryResult recovery variance result
 * @param recoveryStatus recovery variance status
 */
public final class StepRecoveryInstance extends AbstractStepEntity {
    private final StepEntity recoveryDefinition;
    private final String recoveryState;
    private final StepEntity recoveryStartTime;
    private final StepEntity recoveryEndTime;
    private final String recoveryResult;
    private final String recoveryStatus;

    public StepRecoveryInstance(int id, String name, StepEntity recoveryDefinition, String recoveryState, StepEntity recoveryStartTime, StepEntity recoveryEndTime, String recoveryResult, String recoveryStatus) {
        super(id, name);
        this.recoveryDefinition = recoveryDefinition;
        this.recoveryState = recoveryState;
        this.recoveryStartTime = recoveryStartTime;
        this.recoveryEndTime = recoveryEndTime;
        this.recoveryResult = recoveryResult;
        this.recoveryStatus = recoveryStatus;
    }

    public StepEntity getRecoveryDefinition() {
        return recoveryDefinition;
    }

    public String getRecoveryState() {
        return recoveryState;
    }

    public StepEntity getRecoveryStartTime() {
        return recoveryStartTime;
    }

    public StepEntity getRecoveryEndTime() {
        return recoveryEndTime;
    }

    public String getRecoveryResult() {
        return recoveryResult;
    }

    public String getRecoveryStatus() {
        return recoveryStatus;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("recoveryDefinition", recoveryDefinition);
        state.put("recoveryState", recoveryState);
        state.put("recoveryStartTime", recoveryStartTime);
        state.put("recoveryEndTime", recoveryEndTime);
        state.put("recoveryResult", recoveryResult);
        state.put("recoveryStatus", recoveryStatus);
        return state;
    }
}
