package com.minicad.step.model;

import java.util.List;
import java.util.Map;

/**
 * Resolved MODEL_DEFINITION.
 * A model definition entity.
 *
 * @param id STEP instance id
 * @param name model name
 * @param modelType model variance type
 * @param modelGeometry model variance geometry reference
 * @param modelParameters model variance parameters
 * @param modelConstraints model variance constraints
 * @param modelStatus model variance status
 */
public final class StepModelDefinition extends AbstractStepEntity {
    private final String modelType;
    private final StepEntity modelGeometry;
    private final List<String> modelParameters;
    private final List<String> modelConstraints;
    private final String modelStatus;

    public StepModelDefinition(int id, String name, String modelType, StepEntity modelGeometry, List<String> modelParameters, List<String> modelConstraints, String modelStatus) {
        super(id, name);
        this.modelType = modelType;
        this.modelGeometry = modelGeometry;
        this.modelParameters = modelParameters == null ? null : java.util.List.copyOf(modelParameters);
        this.modelConstraints = modelConstraints == null ? null : java.util.List.copyOf(modelConstraints);
        this.modelStatus = modelStatus;
    }

    public String getModelType() {
        return modelType;
    }

    public StepEntity getModelGeometry() {
        return modelGeometry;
    }

    public List<String> getModelParameters() {
        return modelParameters;
    }

    public List<String> getModelConstraints() {
        return modelConstraints;
    }

    public String getModelStatus() {
        return modelStatus;
    }

    @Override
    protected Map<String, Object> components() {
        return componentsOf(
                "id", getId(), "name", getName(), "modelType", modelType, "modelGeometry", modelGeometry,
                "modelParameters", modelParameters, "modelConstraints", modelConstraints,
                "modelStatus", modelStatus);
    }
}
