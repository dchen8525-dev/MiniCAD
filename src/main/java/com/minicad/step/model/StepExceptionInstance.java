package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Resolved EXCEPTION_INSTANCE.
 * An exception instance entity.
 *
 * @param id STEP instance id
 * @param name exception instance name
 * @param exceptionDefinition exception variance definition reference
 * @param exceptionContext exception variance context
 * @param exceptionTime exception variance occurrence time
 * @param exceptionHandled exception variance handled flag
 * @param exceptionStatus exception variance status
 */
public final class StepExceptionInstance extends AbstractStepEntity {
    private final StepEntity exceptionDefinition;
    private final String exceptionContext;
    private final StepEntity exceptionTime;
    private final boolean exceptionHandled;
    private final String exceptionStatus;

    public StepExceptionInstance(int id, String name, StepEntity exceptionDefinition, String exceptionContext, StepEntity exceptionTime, boolean exceptionHandled, String exceptionStatus) {
        super(id, name);
        this.exceptionDefinition = exceptionDefinition;
        this.exceptionContext = exceptionContext;
        this.exceptionTime = exceptionTime;
        this.exceptionHandled = exceptionHandled;
        this.exceptionStatus = exceptionStatus;
    }

    public StepEntity getExceptionDefinition() {
        return exceptionDefinition;
    }

    public String getExceptionContext() {
        return exceptionContext;
    }

    public StepEntity getExceptionTime() {
        return exceptionTime;
    }

    public boolean isExceptionHandled() {
        return exceptionHandled;
    }

    public String getExceptionStatus() {
        return exceptionStatus;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("exceptionDefinition", exceptionDefinition);
        state.put("exceptionContext", exceptionContext);
        state.put("exceptionTime", exceptionTime);
        state.put("exceptionHandled", exceptionHandled);
        state.put("exceptionStatus", exceptionStatus);
        return state;
    }
}
