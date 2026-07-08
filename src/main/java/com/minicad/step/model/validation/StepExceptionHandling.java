package com.minicad.step.model.validation;

import com.minicad.step.model.core.base.StepEntity;
import java.util.List;
import java.util.Objects;

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
public final class StepExceptionHandling implements StepEntity {
    private final int id;
    private final String name;
    private final String varianceException;
    private final String varianceCondition;
    private final StepEntity varianceAction;
    private final StepEntity varianceNotification;
    private final StepEntity varianceLogging;
    private final String varianceStatus;

    public StepExceptionHandling(int id, String name, String varianceException, String varianceCondition, StepEntity varianceAction, StepEntity varianceNotification, StepEntity varianceLogging, String varianceStatus) {
        this.id = id;
        this.name = name;
        this.varianceException = varianceException;
        this.varianceCondition = varianceCondition;
        this.varianceAction = varianceAction;
        this.varianceNotification = varianceNotification;
        this.varianceLogging = varianceLogging;
        this.varianceStatus = varianceStatus;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepExceptionHandling that = (StepExceptionHandling) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(varianceException, that.varianceException) && Objects.equals(varianceCondition, that.varianceCondition) && Objects.equals(varianceAction, that.varianceAction) && Objects.equals(varianceNotification, that.varianceNotification) && Objects.equals(varianceLogging, that.varianceLogging) && Objects.equals(varianceStatus, that.varianceStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, varianceException, varianceCondition, varianceAction, varianceNotification, varianceLogging, varianceStatus);
    }

    @Override
    public String toString() {
        return "StepExceptionHandling{" + "id=" + id + "name=" + name + "varianceException=" + varianceException + "varianceCondition=" + varianceCondition + "varianceAction=" + varianceAction + "varianceNotification=" + varianceNotification + "varianceLogging=" + varianceLogging + "varianceStatus=" + varianceStatus + "}";
    }
}