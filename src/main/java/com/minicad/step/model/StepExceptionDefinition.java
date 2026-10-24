package com.minicad.step.model;

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
        return componentsOf(
                "id", getId(), "name", getName(), "exceptionType", exceptionType,
                "exceptionCode", exceptionCode, "exceptionDescription", exceptionDescription,
                "exceptionHandler", exceptionHandler, "exceptionStatus", exceptionStatus);
    }
}
