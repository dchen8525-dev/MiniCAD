package com.minicad.step.model.product;

import com.minicad.step.model.base.StepEntity;
import java.util.List;
import java.util.Objects;

/**
 * Resolved CAD_MODEL_REFERENCE.
 * A CAD model reference entity.
 *
 * @param id STEP instance id
 * @param name reference name
 * @param modelId model identifier
 * @param modelType model type (3D, 2D, assembly)
 * @param modelGeometry model geometry reference
 * @param modelVersion model version reference
 * @param modelAuthor model author reference
 * @param modelStatus model status
 */
/**
 * Resolved CAD_MODEL_REFERENCE.
 * A CAD model reference entity.
 *
 * @param id STEP instance id
 * @param name reference name
 * @param modelId model identifier
 * @param modelType model type (3D, 2D, assembly)
 * @param modelGeometry model geometry reference
 * @param modelVersion model version reference
 * @param modelAuthor model author reference
 * @param modelStatus model status
 */
public final class StepCadModelReference implements StepEntity {
    private final int id;
    private final String name;
    private final String modelId;
    private final String modelType;
    private final StepEntity modelGeometry;
    private final StepEntity modelVersion;
    private final StepEntity modelAuthor;
    private final String modelStatus;

    public StepCadModelReference(int id, String name, String modelId, String modelType, StepEntity modelGeometry, StepEntity modelVersion, StepEntity modelAuthor, String modelStatus) {
        this.id = id;
        this.name = name;
        this.modelId = modelId;
        this.modelType = modelType;
        this.modelGeometry = modelGeometry;
        this.modelVersion = modelVersion;
        this.modelAuthor = modelAuthor;
        this.modelStatus = modelStatus;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getModelId() {
        return modelId;
    }

    public String getModelType() {
        return modelType;
    }

    public StepEntity getModelGeometry() {
        return modelGeometry;
    }

    public StepEntity getModelVersion() {
        return modelVersion;
    }

    public StepEntity getModelAuthor() {
        return modelAuthor;
    }

    public String getModelStatus() {
        return modelStatus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepCadModelReference that = (StepCadModelReference) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(modelId, that.modelId) && Objects.equals(modelType, that.modelType) && Objects.equals(modelGeometry, that.modelGeometry) && Objects.equals(modelVersion, that.modelVersion) && Objects.equals(modelAuthor, that.modelAuthor) && Objects.equals(modelStatus, that.modelStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, modelId, modelType, modelGeometry, modelVersion, modelAuthor, modelStatus);
    }

    @Override
    public String toString() {
        return "StepCadModelReference{" + "id=" + id + "name=" + name + "modelId=" + modelId + "modelType=" + modelType + "modelGeometry=" + modelGeometry + "modelVersion=" + modelVersion + "modelAuthor=" + modelAuthor + "modelStatus=" + modelStatus + "}";
    }
}