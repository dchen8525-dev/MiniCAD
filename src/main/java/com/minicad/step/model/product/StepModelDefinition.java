package com.minicad.step.model.product;

import com.minicad.step.model.base.StepEntity;
import java.util.List;
import java.util.Objects;

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
public final class StepModelDefinition implements StepEntity {
    private final int id;
    private final String name;
    private final String modelType;
    private final StepEntity modelGeometry;
    private final List<String> modelParameters;
    private final List<String> modelConstraints;
    private final String modelStatus;

    public StepModelDefinition(int id, String name, String modelType, StepEntity modelGeometry, List<String> modelParameters, List<String> modelConstraints, String modelStatus) {
        this.id = id;
        this.name = name;
        this.modelType = modelType;
        this.modelGeometry = modelGeometry;
        this.modelParameters = modelParameters == null ? null : java.util.List.copyOf(modelParameters);
        this.modelConstraints = modelConstraints == null ? null : java.util.List.copyOf(modelConstraints);
        this.modelStatus = modelStatus;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepModelDefinition that = (StepModelDefinition) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(modelType, that.modelType) && Objects.equals(modelGeometry, that.modelGeometry) && Objects.equals(modelParameters, that.modelParameters) && Objects.equals(modelConstraints, that.modelConstraints) && Objects.equals(modelStatus, that.modelStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, modelType, modelGeometry, modelParameters, modelConstraints, modelStatus);
    }

    @Override
    public String toString() {
        return "StepModelDefinition{" + "id=" + id + "name=" + name + "modelType=" + modelType + "modelGeometry=" + modelGeometry + "modelParameters=" + modelParameters + "modelConstraints=" + modelConstraints + "modelStatus=" + modelStatus + "}";
    }
}