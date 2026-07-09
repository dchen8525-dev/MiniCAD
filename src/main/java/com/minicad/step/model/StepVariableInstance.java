package com.minicad.step.model.action;

import com.minicad.step.model.core.base.StepEntity;
import java.util.List;
import java.util.Objects;

/**
 * Resolved VARIABLE_INSTANCE.
 * A variable instance entity.
 *
 * @param id STEP instance id
 * @param name variable instance name
 * @param variableDefinition variable variance definition reference
 * @param variableValue variable variance current value
 * @param variableHistory variable variance value history
 * @param variableStatus variable variance status
 */
/**
 * Resolved VARIABLE_INSTANCE.
 * A variable instance entity.
 *
 * @param id STEP instance id
 * @param name variable instance name
 * @param variableDefinition variable variance definition reference
 * @param variableValue variable variance current value
 * @param variableHistory variable variance value history
 * @param variableStatus variable variance status
 */
public final class StepVariableInstance implements StepEntity {
    private final int id;
    private final String name;
    private final StepEntity variableDefinition;
    private final String variableValue;
    private final List<String> variableHistory;
    private final String variableStatus;

    public StepVariableInstance(int id, String name, StepEntity variableDefinition, String variableValue, List<String> variableHistory, String variableStatus) {
        this.id = id;
        this.name = name;
        this.variableDefinition = variableDefinition;
        this.variableValue = variableValue;
        this.variableHistory = variableHistory == null ? null : java.util.List.copyOf(variableHistory);
        this.variableStatus = variableStatus;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public StepEntity getVariableDefinition() {
        return variableDefinition;
    }

    public String getVariableValue() {
        return variableValue;
    }

    public List<String> getVariableHistory() {
        return variableHistory;
    }

    public String getVariableStatus() {
        return variableStatus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepVariableInstance that = (StepVariableInstance) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(variableDefinition, that.variableDefinition) && Objects.equals(variableValue, that.variableValue) && Objects.equals(variableHistory, that.variableHistory) && Objects.equals(variableStatus, that.variableStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, variableDefinition, variableValue, variableHistory, variableStatus);
    }

    @Override
    public String toString() {
        return "StepVariableInstance{" + "id=" + id + "name=" + name + "variableDefinition=" + variableDefinition + "variableValue=" + variableValue + "variableHistory=" + variableHistory + "variableStatus=" + variableStatus + "}";
    }
}