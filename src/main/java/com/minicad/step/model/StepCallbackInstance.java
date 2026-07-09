package com.minicad.step.model;

import com.minicad.step.model.core.base.StepEntity;
import java.util.List;
import java.util.Objects;

/**
 * Resolved CALLBACK_INSTANCE.
 * A callback instance entity.
 *
 * @param id STEP instance id
 * @param name callback instance name
 * @param callbackDefinition callback variance definition reference
 * @param callbackState callback variance state
 * @param callbackResult callback variance result
 * @param callbackExecuted callback variance executed flag
 * @param callbackStatus callback variance status
 */
/**
 * Resolved CALLBACK_INSTANCE.
 * A callback instance entity.
 *
 * @param id STEP instance id
 * @param name callback instance name
 * @param callbackDefinition callback variance definition reference
 * @param callbackState callback variance state
 * @param callbackResult callback variance result
 * @param callbackExecuted callback variance executed flag
 * @param callbackStatus callback variance status
 */
public final class StepCallbackInstance implements StepEntity {
    private final int id;
    private final String name;
    private final StepEntity callbackDefinition;
    private final String callbackState;
    private final String callbackResult;
    private final boolean callbackExecuted;
    private final String callbackStatus;

    public StepCallbackInstance(int id, String name, StepEntity callbackDefinition, String callbackState, String callbackResult, boolean callbackExecuted, String callbackStatus) {
        this.id = id;
        this.name = name;
        this.callbackDefinition = callbackDefinition;
        this.callbackState = callbackState;
        this.callbackResult = callbackResult;
        this.callbackExecuted = callbackExecuted;
        this.callbackStatus = callbackStatus;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public StepEntity getCallbackDefinition() {
        return callbackDefinition;
    }

    public String getCallbackState() {
        return callbackState;
    }

    public String getCallbackResult() {
        return callbackResult;
    }

    public boolean isCallbackExecuted() {
        return callbackExecuted;
    }

    public String getCallbackStatus() {
        return callbackStatus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepCallbackInstance that = (StepCallbackInstance) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(callbackDefinition, that.callbackDefinition) && Objects.equals(callbackState, that.callbackState) && Objects.equals(callbackResult, that.callbackResult) && callbackExecuted == that.callbackExecuted && Objects.equals(callbackStatus, that.callbackStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, callbackDefinition, callbackState, callbackResult, callbackExecuted, callbackStatus);
    }

    @Override
    public String toString() {
        return "StepCallbackInstance{" + "id=" + id + "name=" + name + "callbackDefinition=" + callbackDefinition + "callbackState=" + callbackState + "callbackResult=" + callbackResult + "callbackExecuted=" + callbackExecuted + "callbackStatus=" + callbackStatus + "}";
    }
}