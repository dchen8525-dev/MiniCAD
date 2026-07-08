package com.minicad.step.model.product;

import com.minicad.step.model.core.base.StepEntity;
import java.util.List;
import java.util.Objects;

/**
 * Resolved ASSEMBLY_OPERATION.
 * An assembly operation entity.
 *
 * @param id STEP instance id
 * @param name operation name
 * @param operationType assembly operation type (fasten, insert, align, weld)
 * @param operationParameters operation parameters
 * @param components components involved in operation
 * @param toolRequirement tool requirement reference
 * @param fixtureRequirement fixture requirement reference
 * @param operationTime estimated operation time
 */
/**
 * Resolved ASSEMBLY_OPERATION.
 * An assembly operation entity.
 *
 * @param id STEP instance id
 * @param name operation name
 * @param operationType assembly operation type (fasten, insert, align, weld)
 * @param operationParameters operation parameters
 * @param components components involved in operation
 * @param toolRequirement tool requirement reference
 * @param fixtureRequirement fixture requirement reference
 * @param operationTime estimated operation time
 */
public final class StepAssemblyOperation implements StepEntity {
    private final int id;
    private final String name;
    private final String operationType;
    private final List<Double> operationParameters;
    private final List<StepEntity> components;
    private final StepEntity toolRequirement;
    private final StepEntity fixtureRequirement;
    private final double operationTime;

    public StepAssemblyOperation(int id, String name, String operationType, List<Double> operationParameters, List<StepEntity> components, StepEntity toolRequirement, StepEntity fixtureRequirement, double operationTime) {
        this.id = id;
        this.name = name;
        this.operationType = operationType;
        this.operationParameters = operationParameters == null ? null : java.util.List.copyOf(operationParameters);
        this.components = components == null ? null : java.util.List.copyOf(components);
        this.toolRequirement = toolRequirement;
        this.fixtureRequirement = fixtureRequirement;
        this.operationTime = operationTime;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getOperationType() {
        return operationType;
    }

    public List<Double> getOperationParameters() {
        return operationParameters;
    }

    public List<StepEntity> getComponents() {
        return components;
    }

    public StepEntity getToolRequirement() {
        return toolRequirement;
    }

    public StepEntity getFixtureRequirement() {
        return fixtureRequirement;
    }

    public double getOperationTime() {
        return operationTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepAssemblyOperation that = (StepAssemblyOperation) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(operationType, that.operationType) && Objects.equals(operationParameters, that.operationParameters) && Objects.equals(components, that.components) && Objects.equals(toolRequirement, that.toolRequirement) && Objects.equals(fixtureRequirement, that.fixtureRequirement) && operationTime == that.operationTime;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, operationType, operationParameters, components, toolRequirement, fixtureRequirement, operationTime);
    }

    @Override
    public String toString() {
        return "StepAssemblyOperation{" + "id=" + id + "name=" + name + "operationType=" + operationType + "operationParameters=" + operationParameters + "components=" + components + "toolRequirement=" + toolRequirement + "fixtureRequirement=" + fixtureRequirement + "operationTime=" + operationTime + "}";
    }
}