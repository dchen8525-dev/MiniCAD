package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Resolved CALLBACK_INSTANCE.
 * A callback instance entity.
 *
 * @param id STEP instance id
 * @param name callback instance name
 * @param callbackDefinition callback variance definition reference
 * @param callbackState callback variance state
 * @param callbackResult callback variance result
 * @param callbackExecuted callback variance executed flag
 * @param callbackStatus callback variance status
 */
public final class StepCallbackInstance extends AbstractStepEntity {
    private final StepEntity callbackDefinition;
    private final String callbackState;
    private final String callbackResult;
    private final boolean callbackExecuted;
    private final String callbackStatus;

    public StepCallbackInstance(int id, String name, StepEntity callbackDefinition, String callbackState, String callbackResult, boolean callbackExecuted, String callbackStatus) {
        super(id, name);
        this.callbackDefinition = callbackDefinition;
        this.callbackState = callbackState;
        this.callbackResult = callbackResult;
        this.callbackExecuted = callbackExecuted;
        this.callbackStatus = callbackStatus;
    }

    public StepEntity getCallbackDefinition() {
        return callbackDefinition;
    }

    public String getCallbackState() {
        return callbackState;
    }

    public String getCallbackResult() {
        return callbackResult;
    }

    public boolean isCallbackExecuted() {
        return callbackExecuted;
    }

    public String getCallbackStatus() {
        return callbackStatus;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("callbackDefinition", callbackDefinition);
        state.put("callbackState", callbackState);
        state.put("callbackResult", callbackResult);
        state.put("callbackExecuted", callbackExecuted);
        state.put("callbackStatus", callbackStatus);
        return state;
    }
}
