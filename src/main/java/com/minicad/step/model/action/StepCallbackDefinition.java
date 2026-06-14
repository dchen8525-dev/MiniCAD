package com.minicad.step.model.action;

import com.minicad.step.model.base.StepEntity;
import java.util.List;
import java.util.Objects;

/**
 * Resolved CALLBACK_DEFINITION.
 * A callback definition entity.
 *
 * @param id STEP instance id
 * @param name callback name
 * @param callbackType callback variance type
 * @param callbackFunction callback variance function reference
 * @param callbackParameters callback variance parameters
 * @param callbackAsync callback variance async flag
 * @param callbackStatus callback variance status
 */
/**
 * Resolved CALLBACK_DEFINITION.
 * A callback definition entity.
 *
 * @param id STEP instance id
 * @param name callback name
 * @param callbackType callback variance type
 * @param callbackFunction callback variance function reference
 * @param callbackParameters callback variance parameters
 * @param callbackAsync callback variance async flag
 * @param callbackStatus callback variance status
 */
public final class StepCallbackDefinition implements StepEntity {
    private final int id;
    private final String name;
    private final String callbackType;
    private final StepEntity callbackFunction;
    private final List<String> callbackParameters;
    private final boolean callbackAsync;
    private final String callbackStatus;

    public StepCallbackDefinition(int id, String name, String callbackType, StepEntity callbackFunction, List<String> callbackParameters, boolean callbackAsync, String callbackStatus) {
        this.id = id;
        this.name = name;
        this.callbackType = callbackType;
        this.callbackFunction = callbackFunction;
        this.callbackParameters = callbackParameters == null ? null : java.util.List.copyOf(callbackParameters);
        this.callbackAsync = callbackAsync;
        this.callbackStatus = callbackStatus;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getCallbackType() {
        return callbackType;
    }

    public StepEntity getCallbackFunction() {
        return callbackFunction;
    }

    public List<String> getCallbackParameters() {
        return callbackParameters;
    }

    public boolean isCallbackAsync() {
        return callbackAsync;
    }

    public String getCallbackStatus() {
        return callbackStatus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepCallbackDefinition that = (StepCallbackDefinition) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(callbackType, that.callbackType) && Objects.equals(callbackFunction, that.callbackFunction) && Objects.equals(callbackParameters, that.callbackParameters) && callbackAsync == that.callbackAsync && Objects.equals(callbackStatus, that.callbackStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, callbackType, callbackFunction, callbackParameters, callbackAsync, callbackStatus);
    }

    @Override
    public String toString() {
        return "StepCallbackDefinition{" + "id=" + id + "name=" + name + "callbackType=" + callbackType + "callbackFunction=" + callbackFunction + "callbackParameters=" + callbackParameters + "callbackAsync=" + callbackAsync + "callbackStatus=" + callbackStatus + "}";
    }
}