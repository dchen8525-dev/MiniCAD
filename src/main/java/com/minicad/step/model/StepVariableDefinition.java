package com.minicad.step.model;

import com.minicad.step.model.core.base.StepEntity;
import java.util.List;
import java.util.Objects;

/**
 * Resolved VARIABLE_DEFINITION.
 * A variable definition entity.
 *
 * @param id STEP instance id
 * @param name variable name
 * @param variableType variable variance type
 * @param variableDataType variable variance data type
 * @param variableScope variable variance scope (local/global)
 * @param variableInitial variable variance initial value
 * @param variableStatus variable variance status
 */
/**
 * Resolved VARIABLE_DEFINITION.
 * A variable definition entity.
 *
 * @param id STEP instance id
 * @param name variable name
 * @param variableType variable variance type
 * @param variableDataType variable variance data type
 * @param variableScope variable variance scope (local/global)
 * @param variableInitial variable variance initial value
 * @param variableStatus variable variance status
 */
public final class StepVariableDefinition implements StepEntity {
    private final int id;
    private final String name;
    private final String variableType;
    private final String variableDataType;
    private final String variableScope;
    private final String variableInitial;
    private final String variableStatus;

    public StepVariableDefinition(int id, String name, String variableType, String variableDataType, String variableScope, String variableInitial, String variableStatus) {
        this.id = id;
        this.name = name;
        this.variableType = variableType;
        this.variableDataType = variableDataType;
        this.variableScope = variableScope;
        this.variableInitial = variableInitial;
        this.variableStatus = variableStatus;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getVariableType() {
        return variableType;
    }

    public String getVariableDataType() {
        return variableDataType;
    }

    public String getVariableScope() {
        return variableScope;
    }

    public String getVariableInitial() {
        return variableInitial;
    }

    public String getVariableStatus() {
        return variableStatus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepVariableDefinition that = (StepVariableDefinition) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(variableType, that.variableType) && Objects.equals(variableDataType, that.variableDataType) && Objects.equals(variableScope, that.variableScope) && Objects.equals(variableInitial, that.variableInitial) && Objects.equals(variableStatus, that.variableStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, variableType, variableDataType, variableScope, variableInitial, variableStatus);
    }

    @Override
    public String toString() {
        return "StepVariableDefinition{" + "id=" + id + "name=" + name + "variableType=" + variableType + "variableDataType=" + variableDataType + "variableScope=" + variableScope + "variableInitial=" + variableInitial + "variableStatus=" + variableStatus + "}";
    }
}