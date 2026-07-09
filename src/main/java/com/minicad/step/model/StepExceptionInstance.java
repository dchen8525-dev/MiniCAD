package com.minicad.step.model;

import com.minicad.step.model.StepEntity;
import java.util.List;
import java.util.Objects;

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
public final class StepExceptionInstance implements StepEntity {
    private final int id;
    private final String name;
    private final StepEntity exceptionDefinition;
    private final String exceptionContext;
    private final StepEntity exceptionTime;
    private final boolean exceptionHandled;
    private final String exceptionStatus;

    public StepExceptionInstance(int id, String name, StepEntity exceptionDefinition, String exceptionContext, StepEntity exceptionTime, boolean exceptionHandled, String exceptionStatus) {
        this.id = id;
        this.name = name;
        this.exceptionDefinition = exceptionDefinition;
        this.exceptionContext = exceptionContext;
        this.exceptionTime = exceptionTime;
        this.exceptionHandled = exceptionHandled;
        this.exceptionStatus = exceptionStatus;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepExceptionInstance that = (StepExceptionInstance) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(exceptionDefinition, that.exceptionDefinition) && Objects.equals(exceptionContext, that.exceptionContext) && Objects.equals(exceptionTime, that.exceptionTime) && exceptionHandled == that.exceptionHandled && Objects.equals(exceptionStatus, that.exceptionStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, exceptionDefinition, exceptionContext, exceptionTime, exceptionHandled, exceptionStatus);
    }

    @Override
    public String toString() {
        return "StepExceptionInstance{" + "id=" + id + "name=" + name + "exceptionDefinition=" + exceptionDefinition + "exceptionContext=" + exceptionContext + "exceptionTime=" + exceptionTime + "exceptionHandled=" + exceptionHandled + "exceptionStatus=" + exceptionStatus + "}";
    }
}