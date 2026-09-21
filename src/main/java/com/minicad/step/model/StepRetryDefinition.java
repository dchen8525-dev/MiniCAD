package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Resolved RETRY_DEFINITION.
 * A retry definition entity.
 *
 * @param id STEP instance id
 * @param name retry name
 * @param retryType retry variance type
 * @param retryMaxAttempts retry variance max attempts
 * @param retryDelay retry variance delay between attempts
 * @param retryBackoff retry variance backoff strategy
 * @param retryStatus retry variance status
 */
public final class StepRetryDefinition extends AbstractStepEntity {
    private final String retryType;
    private final int retryMaxAttempts;
    private final int retryDelay;
    private final String retryBackoff;
    private final String retryStatus;

    public StepRetryDefinition(int id, String name, String retryType, int retryMaxAttempts, int retryDelay, String retryBackoff, String retryStatus) {
        super(id, name);
        this.retryType = retryType;
        this.retryMaxAttempts = retryMaxAttempts;
        this.retryDelay = retryDelay;
        this.retryBackoff = retryBackoff;
        this.retryStatus = retryStatus;
    }

    public String getRetryType() {
        return retryType;
    }

    public int getRetryMaxAttempts() {
        return retryMaxAttempts;
    }

    public int getRetryDelay() {
        return retryDelay;
    }

    public String getRetryBackoff() {
        return retryBackoff;
    }

    public String getRetryStatus() {
        return retryStatus;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("retryType", retryType);
        state.put("retryMaxAttempts", retryMaxAttempts);
        state.put("retryDelay", retryDelay);
        state.put("retryBackoff", retryBackoff);
        state.put("retryStatus", retryStatus);
        return state;
    }
}
