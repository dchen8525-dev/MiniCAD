package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Resolved ERROR_DEFINITION.
 * An error definition entity.
 *
 * @param id STEP instance id
 * @param name error name
 * @param errorType error variance type
 * @param errorCode error variance code
 * @param errorDescription error variance description
 * @param errorSeverity error variance severity level
 * @param errorStatus error variance status
 */
public final class StepErrorDefinition extends AbstractStepEntity {
    private final String errorType;
    private final String errorCode;
    private final String errorDescription;
    private final int errorSeverity;
    private final String errorStatus;

    public StepErrorDefinition(int id, String name, String errorType, String errorCode, String errorDescription, int errorSeverity, String errorStatus) {
        super(id, name);
        this.errorType = errorType;
        this.errorCode = errorCode;
        this.errorDescription = errorDescription;
        this.errorSeverity = errorSeverity;
        this.errorStatus = errorStatus;
    }

    public String getErrorType() {
        return errorType;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public String getErrorDescription() {
        return errorDescription;
    }

    public int getErrorSeverity() {
        return errorSeverity;
    }

    public String getErrorStatus() {
        return errorStatus;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("errorType", errorType);
        state.put("errorCode", errorCode);
        state.put("errorDescription", errorDescription);
        state.put("errorSeverity", errorSeverity);
        state.put("errorStatus", errorStatus);
        return state;
    }
}
