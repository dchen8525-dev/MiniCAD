package com.minicad.step.model;

import com.minicad.step.model.core.base.StepEntity;
import java.util.List;
import java.util.Objects;

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
public final class StepFunctionInstance implements StepEntity {
    private final int id;
    private final String name;
    private final StepEntity functionDefinition;
    private final String functionState;
    private final int functionCallCount;
    private final String functionLastError;
    private final String functionStatus;

    public StepFunctionInstance(int id, String name, StepEntity functionDefinition, String functionState, int functionCallCount, String functionLastError, String functionStatus) {
        this.id = id;
        this.name = name;
        this.functionDefinition = functionDefinition;
        this.functionState = functionState;
        this.functionCallCount = functionCallCount;
        this.functionLastError = functionLastError;
        this.functionStatus = functionStatus;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepFunctionInstance that = (StepFunctionInstance) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(functionDefinition, that.functionDefinition) && Objects.equals(functionState, that.functionState) && functionCallCount == that.functionCallCount && Objects.equals(functionLastError, that.functionLastError) && Objects.equals(functionStatus, that.functionStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, functionDefinition, functionState, functionCallCount, functionLastError, functionStatus);
    }

    @Override
    public String toString() {
        return "StepFunctionInstance{" + "id=" + id + "name=" + name + "functionDefinition=" + functionDefinition + "functionState=" + functionState + "functionCallCount=" + functionCallCount + "functionLastError=" + functionLastError + "functionStatus=" + functionStatus + "}";
    }
}