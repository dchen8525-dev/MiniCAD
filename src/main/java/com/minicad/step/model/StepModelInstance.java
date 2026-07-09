package com.minicad.step.model;

import com.minicad.step.model.core.base.StepEntity;
import java.util.List;
import java.util.Objects;

/**
 * Resolved MODEL_INSTANCE.
 * A model instance entity.
 *
 * @param id STEP instance id
 * @param name model instance name
 * @param modelDefinition model variance definition reference
 * @param modelState model variance state
 * @param modelVersion model variance version
 * @param modelProperties model variance properties
 * @param modelStatus model variance status
 */
/**
 * Resolved MODEL_INSTANCE.
 * A model instance entity.
 *
 * @param id STEP instance id
 * @param name model instance name
 * @param modelDefinition model variance definition reference
 * @param modelState model variance state
 * @param modelVersion model variance version
 * @param modelProperties model variance properties
 * @param modelStatus model variance status
 */
public final class StepModelInstance implements StepEntity {
    private final int id;
    private final String name;
    private final StepEntity modelDefinition;
    private final String modelState;
    private final String modelVersion;
    private final List<String> modelProperties;
    private final String modelStatus;

    public StepModelInstance(int id, String name, StepEntity modelDefinition, String modelState, String modelVersion, List<String> modelProperties, String modelStatus) {
        this.id = id;
        this.name = name;
        this.modelDefinition = modelDefinition;
        this.modelState = modelState;
        this.modelVersion = modelVersion;
        this.modelProperties = modelProperties == null ? null : java.util.List.copyOf(modelProperties);
        this.modelStatus = modelStatus;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public StepEntity getModelDefinition() {
        return modelDefinition;
    }

    public String getModelState() {
        return modelState;
    }

    public String getModelVersion() {
        return modelVersion;
    }

    public List<String> getModelProperties() {
        return modelProperties;
    }

    public String getModelStatus() {
        return modelStatus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepModelInstance that = (StepModelInstance) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(modelDefinition, that.modelDefinition) && Objects.equals(modelState, that.modelState) && Objects.equals(modelVersion, that.modelVersion) && Objects.equals(modelProperties, that.modelProperties) && Objects.equals(modelStatus, that.modelStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, modelDefinition, modelState, modelVersion, modelProperties, modelStatus);
    }

    @Override
    public String toString() {
        return "StepModelInstance{" + "id=" + id + "name=" + name + "modelDefinition=" + modelDefinition + "modelState=" + modelState + "modelVersion=" + modelVersion + "modelProperties=" + modelProperties + "modelStatus=" + modelStatus + "}";
    }
}