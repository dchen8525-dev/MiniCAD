package com.minicad.step.model;

import com.minicad.step.model.core.base.StepEntity;
import java.util.List;
import java.util.Objects;

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
public final class StepErrorInstance implements StepEntity {
    private final int id;
    private final String name;
    private final StepEntity errorDefinition;
    private final String errorContext;
    private final StepEntity errorTime;
    private final String errorStackTrace;
    private final boolean errorResolved;
    private final String errorStatus;

    public StepErrorInstance(int id, String name, StepEntity errorDefinition, String errorContext, StepEntity errorTime, String errorStackTrace, boolean errorResolved, String errorStatus) {
        this.id = id;
        this.name = name;
        this.errorDefinition = errorDefinition;
        this.errorContext = errorContext;
        this.errorTime = errorTime;
        this.errorStackTrace = errorStackTrace;
        this.errorResolved = errorResolved;
        this.errorStatus = errorStatus;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepErrorInstance that = (StepErrorInstance) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(errorDefinition, that.errorDefinition) && Objects.equals(errorContext, that.errorContext) && Objects.equals(errorTime, that.errorTime) && Objects.equals(errorStackTrace, that.errorStackTrace) && errorResolved == that.errorResolved && Objects.equals(errorStatus, that.errorStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, errorDefinition, errorContext, errorTime, errorStackTrace, errorResolved, errorStatus);
    }

    @Override
    public String toString() {
        return "StepErrorInstance{" + "id=" + id + "name=" + name + "errorDefinition=" + errorDefinition + "errorContext=" + errorContext + "errorTime=" + errorTime + "errorStackTrace=" + errorStackTrace + "errorResolved=" + errorResolved + "errorStatus=" + errorStatus + "}";
    }
}