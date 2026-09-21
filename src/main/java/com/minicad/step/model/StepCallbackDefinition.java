package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Resolved CALLBACK_DEFINITION.
 * A callback definition entity.
 *
 * @param id STEP instance id
 * @param name callback name
 * @param callbackType callback variance type
 * @param callbackFunction callback variance function reference
 * @param callbackParameters callback variance parameters
 * @param callbackAsync callback variance async flag
 * @param callbackStatus callback variance status
 */
public final class StepCallbackDefinition extends AbstractStepEntity {
    private final String callbackType;
    private final StepEntity callbackFunction;
    private final List<String> callbackParameters;
    private final boolean callbackAsync;
    private final String callbackStatus;

    public StepCallbackDefinition(int id, String name, String callbackType, StepEntity callbackFunction, List<String> callbackParameters, boolean callbackAsync, String callbackStatus) {
        super(id, name);
        this.callbackType = callbackType;
        this.callbackFunction = callbackFunction;
        this.callbackParameters = callbackParameters == null ? null : java.util.List.copyOf(callbackParameters);
        this.callbackAsync = callbackAsync;
        this.callbackStatus = callbackStatus;
    }

    public String getCallbackType() {
        return callbackType;
    }

    public StepEntity getCallbackFunction() {
        return callbackFunction;
    }

    public List<String> getCallbackParameters() {
        return callbackParameters;
    }

    public boolean isCallbackAsync() {
        return callbackAsync;
    }

    public String getCallbackStatus() {
        return callbackStatus;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("callbackType", callbackType);
        state.put("callbackFunction", callbackFunction);
        state.put("callbackParameters", callbackParameters);
        state.put("callbackAsync", callbackAsync);
        state.put("callbackStatus", callbackStatus);
        return state;
    }
}
