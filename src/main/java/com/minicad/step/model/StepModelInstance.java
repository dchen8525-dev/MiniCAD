package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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
public final class StepModelInstance extends AbstractStepEntity {
    private final StepEntity modelDefinition;
    private final String modelState;
    private final String modelVersion;
    private final List<String> modelProperties;
    private final String modelStatus;

    public StepModelInstance(int id, String name, StepEntity modelDefinition, String modelState, String modelVersion, List<String> modelProperties, String modelStatus) {
        super(id, name);
        this.modelDefinition = modelDefinition;
        this.modelState = modelState;
        this.modelVersion = modelVersion;
        this.modelProperties = modelProperties == null ? null : java.util.List.copyOf(modelProperties);
        this.modelStatus = modelStatus;
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
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("modelDefinition", modelDefinition);
        state.put("modelState", modelState);
        state.put("modelVersion", modelVersion);
        state.put("modelProperties", modelProperties);
        state.put("modelStatus", modelStatus);
        return state;
    }
}
