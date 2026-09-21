package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Resolved EXCEPTION_DEFINITION.
 * An exception definition entity.
 *
 * @param id STEP instance id
 * @param name exception name
 * @param exceptionType exception variance type
 * @param exceptionCode exception variance code
 * @param exceptionDescription exception variance description
 * @param exceptionHandler exception variance handler reference
 * @param exceptionStatus exception variance status
 */
public final class StepExceptionDefinition extends AbstractStepEntity {
    private final String exceptionType;
    private final String exceptionCode;
    private final String exceptionDescription;
    private final StepEntity exceptionHandler;
    private final String exceptionStatus;

    public StepExceptionDefinition(int id, String name, String exceptionType, String exceptionCode, String exceptionDescription, StepEntity exceptionHandler, String exceptionStatus) {
        super(id, name);
        this.exceptionType = exceptionType;
        this.exceptionCode = exceptionCode;
        this.exceptionDescription = exceptionDescription;
        this.exceptionHandler = exceptionHandler;
        this.exceptionStatus = exceptionStatus;
    }

    public String getExceptionType() {
        return exceptionType;
    }

    public String getExceptionCode() {
        return exceptionCode;
    }

    public String getExceptionDescription() {
        return exceptionDescription;
    }

    public StepEntity getExceptionHandler() {
        return exceptionHandler;
    }

    public String getExceptionStatus() {
        return exceptionStatus;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("exceptionType", exceptionType);
        state.put("exceptionCode", exceptionCode);
        state.put("exceptionDescription", exceptionDescription);
        state.put("exceptionHandler", exceptionHandler);
        state.put("exceptionStatus", exceptionStatus);
        return state;
    }
}
