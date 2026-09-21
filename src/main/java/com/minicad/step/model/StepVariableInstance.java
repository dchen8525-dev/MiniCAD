package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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
public final class StepVariableInstance extends AbstractStepEntity {
    private final StepEntity variableDefinition;
    private final String variableValue;
    private final List<String> variableHistory;
    private final String variableStatus;

    public StepVariableInstance(int id, String name, StepEntity variableDefinition, String variableValue, List<String> variableHistory, String variableStatus) {
        super(id, name);
        this.variableDefinition = variableDefinition;
        this.variableValue = variableValue;
        this.variableHistory = variableHistory == null ? null : java.util.List.copyOf(variableHistory);
        this.variableStatus = variableStatus;
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
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("variableDefinition", variableDefinition);
        state.put("variableValue", variableValue);
        state.put("variableHistory", variableHistory);
        state.put("variableStatus", variableStatus);
        return state;
    }
}
