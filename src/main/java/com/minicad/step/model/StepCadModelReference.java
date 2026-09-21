package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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
public final class StepCadModelReference extends AbstractStepEntity {
    private final String modelId;
    private final String modelType;
    private final StepEntity modelGeometry;
    private final StepEntity modelVersion;
    private final StepEntity modelAuthor;
    private final String modelStatus;

    public StepCadModelReference(int id, String name, String modelId, String modelType, StepEntity modelGeometry, StepEntity modelVersion, StepEntity modelAuthor, String modelStatus) {
        super(id, name);
        this.modelId = modelId;
        this.modelType = modelType;
        this.modelGeometry = modelGeometry;
        this.modelVersion = modelVersion;
        this.modelAuthor = modelAuthor;
        this.modelStatus = modelStatus;
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
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("modelId", modelId);
        state.put("modelType", modelType);
        state.put("modelGeometry", modelGeometry);
        state.put("modelVersion", modelVersion);
        state.put("modelAuthor", modelAuthor);
        state.put("modelStatus", modelStatus);
        return state;
    }
}
