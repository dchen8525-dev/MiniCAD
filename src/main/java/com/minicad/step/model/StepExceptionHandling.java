package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Resolved EXCEPTION_HANDLING.
 * An exception handling entity.
 *
 * @param id STEP instance id
 * @param name handling name
 * @varianceException exception variance type
 * @varianceCondition exception variance condition
 * @varianceAction exception variance handling action
 * @varianceNotification notification variance requirements
 * @varianceLogging logging variance requirements
 * @varianceStatus handling variance status
 */
public final class StepExceptionHandling extends AbstractStepEntity {
    private final String varianceException;
    private final String varianceCondition;
    private final StepEntity varianceAction;
    private final StepEntity varianceNotification;
    private final StepEntity varianceLogging;
    private final String varianceStatus;

    public StepExceptionHandling(int id, String name, String varianceException, String varianceCondition, StepEntity varianceAction, StepEntity varianceNotification, StepEntity varianceLogging, String varianceStatus) {
        super(id, name);
        this.varianceException = varianceException;
        this.varianceCondition = varianceCondition;
        this.varianceAction = varianceAction;
        this.varianceNotification = varianceNotification;
        this.varianceLogging = varianceLogging;
        this.varianceStatus = varianceStatus;
    }

    public String getVarianceException() {
        return varianceException;
    }

    public String getVarianceCondition() {
        return varianceCondition;
    }

    public StepEntity getVarianceAction() {
        return varianceAction;
    }

    public StepEntity getVarianceNotification() {
        return varianceNotification;
    }

    public StepEntity getVarianceLogging() {
        return varianceLogging;
    }

    public String getVarianceStatus() {
        return varianceStatus;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("varianceException", varianceException);
        state.put("varianceCondition", varianceCondition);
        state.put("varianceAction", varianceAction);
        state.put("varianceNotification", varianceNotification);
        state.put("varianceLogging", varianceLogging);
        state.put("varianceStatus", varianceStatus);
        return state;
    }
}
