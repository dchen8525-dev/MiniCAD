package com.minicad.step.model;

import java.util.List;
import java.util.Map;

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
public final class StepAssemblyOperation extends AbstractStepEntity {
    private final String operationType;
    private final List<Double> operationParameters;
    private final List<StepEntity> components;
    private final StepEntity toolRequirement;
    private final StepEntity fixtureRequirement;
    private final double operationTime;

    public StepAssemblyOperation(int id, String name, String operationType, List<Double> operationParameters, List<StepEntity> components, StepEntity toolRequirement, StepEntity fixtureRequirement, double operationTime) {
        super(id, name);
        this.operationType = operationType;
        this.operationParameters = operationParameters == null ? null : java.util.List.copyOf(operationParameters);
        this.components = components == null ? null : java.util.List.copyOf(components);
        this.toolRequirement = toolRequirement;
        this.fixtureRequirement = fixtureRequirement;
        this.operationTime = operationTime;
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
    protected Map<String, Object> components() {
        return componentsOf(
                "id", getId(), "name", getName(), "operationType", operationType,
                "operationParameters", operationParameters, "components", components,
                "toolRequirement", toolRequirement, "fixtureRequirement", fixtureRequirement,
                "operationTime", operationTime);
    }
}
