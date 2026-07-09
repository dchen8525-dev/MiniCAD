package com.minicad.step.model;

import com.minicad.step.model.StepEntity;
import java.util.List;
import java.util.Objects;

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
public final class StepFunctionDefinition implements StepEntity {
    private final int id;
    private final String name;
    private final String functionType;
    private final String functionDescription;
    private final List<String> functionInputs;
    private final List<String> functionOutputs;
    private final String functionStatus;

    public StepFunctionDefinition(int id, String name, String functionType, String functionDescription, List<String> functionInputs, List<String> functionOutputs, String functionStatus) {
        this.id = id;
        this.name = name;
        this.functionType = functionType;
        this.functionDescription = functionDescription;
        this.functionInputs = functionInputs == null ? null : java.util.List.copyOf(functionInputs);
        this.functionOutputs = functionOutputs == null ? null : java.util.List.copyOf(functionOutputs);
        this.functionStatus = functionStatus;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepFunctionDefinition that = (StepFunctionDefinition) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(functionType, that.functionType) && Objects.equals(functionDescription, that.functionDescription) && Objects.equals(functionInputs, that.functionInputs) && Objects.equals(functionOutputs, that.functionOutputs) && Objects.equals(functionStatus, that.functionStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, functionType, functionDescription, functionInputs, functionOutputs, functionStatus);
    }

    @Override
    public String toString() {
        return "StepFunctionDefinition{" + "id=" + id + "name=" + name + "functionType=" + functionType + "functionDescription=" + functionDescription + "functionInputs=" + functionInputs + "functionOutputs=" + functionOutputs + "functionStatus=" + functionStatus + "}";
    }
}