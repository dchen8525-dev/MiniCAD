package com.minicad.step.model.workflow;

import com.minicad.step.model.core.base.StepEntity;
import java.util.List;
import java.util.Objects;

/**
 * Resolved RETRY_INSTANCE.
 * A retry instance entity.
 *
 * @param id STEP instance id
 * @param name retry instance name
 * @param retryDefinition retry variance definition reference
 * @param retryState retry variance state
 * @param retryAttempt retry variance current attempt
 * @param retryRemaining retry variance remaining attempts
 * @param retryLastError retry variance last error
 * @param retryStatus retry variance status
 */
/**
 * Resolved RETRY_INSTANCE.
 * A retry instance entity.
 *
 * @param id STEP instance id
 * @param name retry instance name
 * @param retryDefinition retry variance definition reference
 * @param retryState retry variance state
 * @param retryAttempt retry variance current attempt
 * @param retryRemaining retry variance remaining attempts
 * @param retryLastError retry variance last error
 * @param retryStatus retry variance status
 */
public final class StepRetryInstance implements StepEntity {
    private final int id;
    private final String name;
    private final StepEntity retryDefinition;
    private final String retryState;
    private final int retryAttempt;
    private final int retryRemaining;
    private final String retryLastError;
    private final String retryStatus;

    public StepRetryInstance(int id, String name, StepEntity retryDefinition, String retryState, int retryAttempt, int retryRemaining, String retryLastError, String retryStatus) {
        this.id = id;
        this.name = name;
        this.retryDefinition = retryDefinition;
        this.retryState = retryState;
        this.retryAttempt = retryAttempt;
        this.retryRemaining = retryRemaining;
        this.retryLastError = retryLastError;
        this.retryStatus = retryStatus;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public StepEntity getRetryDefinition() {
        return retryDefinition;
    }

    public String getRetryState() {
        return retryState;
    }

    public int getRetryAttempt() {
        return retryAttempt;
    }

    public int getRetryRemaining() {
        return retryRemaining;
    }

    public String getRetryLastError() {
        return retryLastError;
    }

    public String getRetryStatus() {
        return retryStatus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepRetryInstance that = (StepRetryInstance) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(retryDefinition, that.retryDefinition) && Objects.equals(retryState, that.retryState) && retryAttempt == that.retryAttempt && retryRemaining == that.retryRemaining && Objects.equals(retryLastError, that.retryLastError) && Objects.equals(retryStatus, that.retryStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, retryDefinition, retryState, retryAttempt, retryRemaining, retryLastError, retryStatus);
    }

    @Override
    public String toString() {
        return "StepRetryInstance{" + "id=" + id + "name=" + name + "retryDefinition=" + retryDefinition + "retryState=" + retryState + "retryAttempt=" + retryAttempt + "retryRemaining=" + retryRemaining + "retryLastError=" + retryLastError + "retryStatus=" + retryStatus + "}";
    }
}