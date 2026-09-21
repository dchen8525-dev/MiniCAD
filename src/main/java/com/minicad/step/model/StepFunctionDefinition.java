package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Resolved FUNCTION_DEFINITION.
 * A function definition entity.
 *
 * @param id STEP instance id
 * @param name function name
 * @param functionType function variance type
 * @param functionDescription function variance description
 * @param functionInputs function variance inputs
 * @param functionOutputs function variance outputs
 * @param functionStatus function variance status
 */
public final class StepFunctionDefinition extends AbstractStepEntity {
    private final String functionType;
    private final String functionDescription;
    private final List<String> functionInputs;
    private final List<String> functionOutputs;
    private final String functionStatus;

    public StepFunctionDefinition(int id, String name, String functionType, String functionDescription, List<String> functionInputs, List<String> functionOutputs, String functionStatus) {
        super(id, name);
        this.functionType = functionType;
        this.functionDescription = functionDescription;
        this.functionInputs = functionInputs == null ? null : java.util.List.copyOf(functionInputs);
        this.functionOutputs = functionOutputs == null ? null : java.util.List.copyOf(functionOutputs);
        this.functionStatus = functionStatus;
    }

    public String getFunctionType() {
        return functionType;
    }

    public String getFunctionDescription() {
        return functionDescription;
    }

    public List<String> getFunctionInputs() {
        return functionInputs;
    }

    public List<String> getFunctionOutputs() {
        return functionOutputs;
    }

    public String getFunctionStatus() {
        return functionStatus;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("functionType", functionType);
        state.put("functionDescription", functionDescription);
        state.put("functionInputs", functionInputs);
        state.put("functionOutputs", functionOutputs);
        state.put("functionStatus", functionStatus);
        return state;
    }
}
