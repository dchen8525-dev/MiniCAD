package com.minicad.step.model;

import java.util.List;
import java.util.Map;

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
public final class StepVariableDefinition extends AbstractStepEntity {
    private final String variableType;
    private final String variableDataType;
    private final String variableScope;
    private final String variableInitial;
    private final String variableStatus;

    public StepVariableDefinition(int id, String name, String variableType, String variableDataType, String variableScope, String variableInitial, String variableStatus) {
        super(id, name);
        this.variableType = variableType;
        this.variableDataType = variableDataType;
        this.variableScope = variableScope;
        this.variableInitial = variableInitial;
        this.variableStatus = variableStatus;
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
    protected Map<String, Object> components() {
        return componentsOf(
                "id", getId(), "name", getName(), "variableType", variableType,
                "variableDataType", variableDataType, "variableScope", variableScope,
                "variableInitial", variableInitial, "variableStatus", variableStatus);
    }
}
