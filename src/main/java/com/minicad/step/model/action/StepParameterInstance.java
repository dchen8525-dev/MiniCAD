package com.minicad.step.model.action;

import com.minicad.step.model.base.StepEntity;
import java.util.List;
import java.util.Objects;

/**
 * Resolved PARAMETER_INSTANCE.
 * A parameter instance entity.
 *
 * @param id STEP instance id
 * @param name parameter instance name
 * @param parameterDefinition parameter variance definition reference
 * @param parameterValue parameter variance current value
 * @param parameterHistory parameter variance value history
 * @param parameterStatus parameter variance status
 */
/**
 * Resolved PARAMETER_INSTANCE.
 * A parameter instance entity.
 *
 * @param id STEP instance id
 * @param name parameter instance name
 * @param parameterDefinition parameter variance definition reference
 * @param parameterValue parameter variance current value
 * @param parameterHistory parameter variance value history
 * @param parameterStatus parameter variance status
 */
public final class StepParameterInstance implements StepEntity {
    private final int id;
    private final String name;
    private final StepEntity parameterDefinition;
    private final String parameterValue;
    private final List<String> parameterHistory;
    private final String parameterStatus;

    public StepParameterInstance(int id, String name, StepEntity parameterDefinition, String parameterValue, List<String> parameterHistory, String parameterStatus) {
        this.id = id;
        this.name = name;
        this.parameterDefinition = parameterDefinition;
        this.parameterValue = parameterValue;
        this.parameterHistory = parameterHistory == null ? null : java.util.List.copyOf(parameterHistory);
        this.parameterStatus = parameterStatus;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public StepEntity getParameterDefinition() {
        return parameterDefinition;
    }

    public String getParameterValue() {
        return parameterValue;
    }

    public List<String> getParameterHistory() {
        return parameterHistory;
    }

    public String getParameterStatus() {
        return parameterStatus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepParameterInstance that = (StepParameterInstance) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(parameterDefinition, that.parameterDefinition) && Objects.equals(parameterValue, that.parameterValue) && Objects.equals(parameterHistory, that.parameterHistory) && Objects.equals(parameterStatus, that.parameterStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, parameterDefinition, parameterValue, parameterHistory, parameterStatus);
    }

    @Override
    public String toString() {
        return "StepParameterInstance{" + "id=" + id + "name=" + name + "parameterDefinition=" + parameterDefinition + "parameterValue=" + parameterValue + "parameterHistory=" + parameterHistory + "parameterStatus=" + parameterStatus + "}";
    }
}