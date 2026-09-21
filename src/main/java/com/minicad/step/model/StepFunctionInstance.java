package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Resolved FUNCTION_INSTANCE.
 * A function instance entity.
 *
 * @param id STEP instance id
 * @param name function instance name
 * @param functionDefinition function variance definition reference
 * @param functionState function variance state
 * @param functionCallCount function variance call count
 * @param functionLastError function variance last error
 * @param functionStatus function variance status
 */
public final class StepFunctionInstance extends AbstractStepEntity {
    private final StepEntity functionDefinition;
    private final String functionState;
    private final int functionCallCount;
    private final String functionLastError;
    private final String functionStatus;

    public StepFunctionInstance(int id, String name, StepEntity functionDefinition, String functionState, int functionCallCount, String functionLastError, String functionStatus) {
        super(id, name);
        this.functionDefinition = functionDefinition;
        this.functionState = functionState;
        this.functionCallCount = functionCallCount;
        this.functionLastError = functionLastError;
        this.functionStatus = functionStatus;
    }

    public StepEntity getFunctionDefinition() {
        return functionDefinition;
    }

    public String getFunctionState() {
        return functionState;
    }

    public int getFunctionCallCount() {
        return functionCallCount;
    }

    public String getFunctionLastError() {
        return functionLastError;
    }

    public String getFunctionStatus() {
        return functionStatus;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("functionDefinition", functionDefinition);
        state.put("functionState", functionState);
        state.put("functionCallCount", functionCallCount);
        state.put("functionLastError", functionLastError);
        state.put("functionStatus", functionStatus);
        return state;
    }
}
