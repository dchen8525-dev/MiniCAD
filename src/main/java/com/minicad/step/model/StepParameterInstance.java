package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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
public final class StepParameterInstance extends AbstractStepEntity {
    private final StepEntity parameterDefinition;
    private final String parameterValue;
    private final List<String> parameterHistory;
    private final String parameterStatus;

    public StepParameterInstance(int id, String name, StepEntity parameterDefinition, String parameterValue, List<String> parameterHistory, String parameterStatus) {
        super(id, name);
        this.parameterDefinition = parameterDefinition;
        this.parameterValue = parameterValue;
        this.parameterHistory = parameterHistory == null ? null : java.util.List.copyOf(parameterHistory);
        this.parameterStatus = parameterStatus;
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
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("parameterDefinition", parameterDefinition);
        state.put("parameterValue", parameterValue);
        state.put("parameterHistory", parameterHistory);
        state.put("parameterStatus", parameterStatus);
        return state;
    }
}
