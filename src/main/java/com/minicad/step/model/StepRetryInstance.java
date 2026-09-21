package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Resolved RETRY_INSTANCE.
 * A retry instance entity.
 *
 * @param id STEP instance id
 * @param name retry instance name
 * @param retryDefinition retry variance definition reference
 * @param retryState retry variance state
 * @param retryAttempt retry variance current attempt
 * @param retryRemaining retry variance remaining attempts
 * @param retryLastError retry variance last error
 * @param retryStatus retry variance status
 */
public final class StepRetryInstance extends AbstractStepEntity {
    private final StepEntity retryDefinition;
    private final String retryState;
    private final int retryAttempt;
    private final int retryRemaining;
    private final String retryLastError;
    private final String retryStatus;

    public StepRetryInstance(int id, String name, StepEntity retryDefinition, String retryState, int retryAttempt, int retryRemaining, String retryLastError, String retryStatus) {
        super(id, name);
        this.retryDefinition = retryDefinition;
        this.retryState = retryState;
        this.retryAttempt = retryAttempt;
        this.retryRemaining = retryRemaining;
        this.retryLastError = retryLastError;
        this.retryStatus = retryStatus;
    }

    public StepEntity getRetryDefinition() {
        return retryDefinition;
    }

    public String getRetryState() {
        return retryState;
    }

    public int getRetryAttempt() {
        return retryAttempt;
    }

    public int getRetryRemaining() {
        return retryRemaining;
    }

    public String getRetryLastError() {
        return retryLastError;
    }

    public String getRetryStatus() {
        return retryStatus;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("retryDefinition", retryDefinition);
        state.put("retryState", retryState);
        state.put("retryAttempt", retryAttempt);
        state.put("retryRemaining", retryRemaining);
        state.put("retryLastError", retryLastError);
        state.put("retryStatus", retryStatus);
        return state;
    }
}
