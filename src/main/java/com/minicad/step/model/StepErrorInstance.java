package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Resolved ERROR_INSTANCE.
 * An error instance entity.
 *
 * @param id STEP instance id
 * @param name error instance name
 * @param errorDefinition error variance definition reference
 * @param errorContext error variance context
 * @param errorTime error variance occurrence time
 * @param errorStackTrace error variance stack trace
 * @param errorResolved error variance resolved flag
 * @param errorStatus error variance status
 */
public final class StepErrorInstance extends AbstractStepEntity {
    private final StepEntity errorDefinition;
    private final String errorContext;
    private final StepEntity errorTime;
    private final String errorStackTrace;
    private final boolean errorResolved;
    private final String errorStatus;

    public StepErrorInstance(int id, String name, StepEntity errorDefinition, String errorContext, StepEntity errorTime, String errorStackTrace, boolean errorResolved, String errorStatus) {
        super(id, name);
        this.errorDefinition = errorDefinition;
        this.errorContext = errorContext;
        this.errorTime = errorTime;
        this.errorStackTrace = errorStackTrace;
        this.errorResolved = errorResolved;
        this.errorStatus = errorStatus;
    }

    public StepEntity getErrorDefinition() {
        return errorDefinition;
    }

    public String getErrorContext() {
        return errorContext;
    }

    public StepEntity getErrorTime() {
        return errorTime;
    }

    public String getErrorStackTrace() {
        return errorStackTrace;
    }

    public boolean isErrorResolved() {
        return errorResolved;
    }

    public String getErrorStatus() {
        return errorStatus;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("errorDefinition", errorDefinition);
        state.put("errorContext", errorContext);
        state.put("errorTime", errorTime);
        state.put("errorStackTrace", errorStackTrace);
        state.put("errorResolved", errorResolved);
        state.put("errorStatus", errorStatus);
        return state;
    }
}
